package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.ClientPasswordEditDto;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final PasswordEncoder passwordEncoder;

    @Value("${GOOGLE_API_KEY}")
    private String googleApiKey;

    @Transactional(readOnly = true)
    public List<ClientsDto.Result> getClientList(){
        List<Clients> allClients = clientRepository.findAll();
        return ClientsDto.Result.of(allClients);
    }

    @Transactional
    public ClientsDto.Result addClients(ClientsDto.Create create){
        if(serviceErrorHelper.isUserIdDuplicated(create.getClientName())){
            ErrorCode duplicateClientError = ErrorCode.DUPLICATE_USER_ID_ERROR;
            throw new BusinessException(duplicateClientError, duplicateClientError.getReason());
        }

        if(serviceErrorHelper.isUserPhoneNumberDuplicated(create.getClientPhNum())){
            ErrorCode duplicatePhoneNumberError = ErrorCode.DUPLICATE_PHONE_NUMBER_ERROR;
            throw new BusinessException(duplicatePhoneNumberError, duplicatePhoneNumberError.getReason());
        }

        Clients createClients = create.asEntity();
        createClients.setClientPassword(passwordEncoder.encode(create.getClientPassword()));
        createClients = setClientLocation(createClients);

        Clients saveClients = clientRepository.save(createClients);


        return ClientsDto.Result.of(saveClients);
    }

    @Transactional(readOnly = true)
    public Clients findClientByUserName(String clientUserName){
        Clients findClients = clientRepository.findClientsByClientName(clientUserName);

        if(findClients==null){
            throw new BusinessException(ErrorCode.CLIENT_NOT_FOUND_ERROR, ErrorCode.CLIENT_NOT_FOUND_ERROR.getReason());
        }

        return findClients;
    }

    @Transactional
    public ClientsDto.Result editClientPassword(ClientPasswordEditDto clientPasswordEditDto){
        int clientId = clientPasswordEditDto.getClientId();
        String clientPassword = clientPasswordEditDto.getClientPassword();

        Clients findClients = serviceErrorHelper.findClientsOrElseThrow404(clientId);
        findClients.setClientPassword(passwordEncoder.encode(clientPassword));

        Clients saveClients = clientRepository.save(findClients);
        return ClientsDto.Result.of(saveClients);
    }

    @Transactional
    public Clients updateClients(ClientsDto.Put put){

        Clients findClients = serviceErrorHelper.findClientsOrElseThrow404(put.getClientId());

        Clients putClients = put.asPutEntity(findClients);
//        putClients.setClientPassword(passwordEncoder.encode(putClients.getClientPassword()));

        putClients = setClientLocation(putClients);
        Clients saveClients = clientRepository.save(putClients);

        return saveClients;

    }

    // Clients -> setClientLocation -> getLocationByClientAd 로 위도, 경도, 도, 시 추출 후 Client로 set 해주는 method
    protected Clients setClientLocation(Clients clients){
        Map<String, String> locationByClientAd = getLocationByClientAd(clients.getClientAddr());

        String lat = locationByClientAd.get("lat");
        String lng = locationByClientAd.get("lng");
        String clientDo = locationByClientAd.get("do");
        String clientSi = locationByClientAd.get("si");

        if (lat != null && lng != null && clientDo != null && clientSi != null) {
            double latDouble = Double.parseDouble(lat);
            double lngDouble = Double.parseDouble(lng);

            clients.setClientLag(roundToDecimalPlaces(latDouble));
            clients.setClientLong(roundToDecimalPlaces(lngDouble));
            clients.setClientDo(clientDo);
            clients.setClientSi(clientSi);

        }
        return clients;
    }

    /**
     * clientAddress -> Geocoding API 로 위도, 경도, 시, 도 추출 -> Map으로 된 데이터 반환
     * @param clientAddress
     * @return Map<String, String>
     */
    public Map<String, String> getLocationByClientAd(String clientAddress){
        try{

            String encodedAddress = URLEncoder.encode(clientAddress, StandardCharsets.UTF_8)
                    .replace("+", "%20");

            String surl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + encodedAddress + "&key=" + googleApiKey + "&language=ko";

            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;

            while((inputStr = streamReader.readLine()) != null){
                responseStrBuilder.append(inputStr);
            }

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

            if (!jsonObject.getString("status").equals("OK")) {
                log.error("Google API Error: " + jsonObject.toString());
                return Collections.emptyMap();
            }

            JSONArray results = jsonObject.getJSONArray("results");
            HashMap<String, String> ret = new HashMap<>();

            if(!results.isEmpty()){
                JSONObject jo = results.getJSONObject(0);

                // 위도 & 경도 추출
                Double lat = jo.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lng = jo.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                JSONArray addressComponents = jo.getJSONArray("address_components");
                String province = "";
                String city = "";

                for(int i=0; i < addressComponents.length(); i++){
                    JSONObject component = addressComponents.getJSONObject(i);
                    JSONArray types = component.getJSONArray("types");
                    String longName = component.getString("long_name");
                    if(types.toString().contains("administrative_area_level_1")){
                        province = longName; // 도
                    }else if(types.toString().contains("locality")){
                        city = longName; // 시
                    }
                }

                ret.put("lat", lat.toString());
                ret.put("lng", lng.toString());
                ret.put("do", province);
                ret.put("si", city);

                return ret;
            }
        }catch (Exception ex) {
            ErrorCode internalServerError = ErrorCode.INTERNAL_SERVER_ERROR;
            log.error(ex.getMessage());
        }
        return null;
    }

    @Transactional(readOnly = true)
    public ClientsDto.Result findClientById(int clientId){
        return ClientsDto.Result.of(serviceErrorHelper.findClientsOrElseThrow404(clientId));

    }


    // 클라이언트 전체 데이터 업데이트를 위한 임시 메서드
//    @Transactional
//    public List<ClientsDto.Result> updateClientLocation(){
//        List<ClientsDto.Result> clientList = getClientList();
//
//        return clientList.stream().map(client -> {
//            ClientsDto.Put putClient = ClientsDto.Put.builder()
//                    .clientAddr(client.getClientAddr())
//                    .clientStatus(client.getClientStatus())
//                    .clientPhNum(client.getClientPhNum())
//                    .clientBusinessNumber(client.getClientBusinessNumber())
//                    .clientCeoName(client.getClientCeoName())
//                    .clientId(client.getClientId())
//                    .clientMarginRatio(client.getClientMarginRatio())
//                    .clientStoreName(client.getClientStoreName())
//                    .build();
//            Clients clients = updateClients(putClient);
//
//            return ClientsDto.Result.of(clients);
//        }).collect(Collectors.toList());
//
//
//    }


    @Transactional
    public void deleteClientsById(int clientId){
        clientRepository.deleteById(clientId);
    }
    protected String roundToDecimalPlaces(double value){
        double doubleValue = new BigDecimal(value).setScale(6, RoundingMode.HALF_UP).doubleValue();
        return Double.toString(doubleValue);
    }

}
