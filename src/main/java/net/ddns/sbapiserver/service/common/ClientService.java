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
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Clients saveClients = clientRepository.save(putClients);

        return saveClients;

    }

    @Transactional
    public Map<String, String> getLocationByClientAd(String clientAddress){
        try{

            String encodedAddress = URLEncoder.encode(clientAddress, StandardCharsets.UTF_8)
                    .replace("+", "%20");

            String surl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + encodedAddress + "&key=" + googleApiKey;
//            log.error("googleApiKey : {}", googleApiKey);
//            log.info("Requesting URL: {}", surl);

            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;

            while((inputStr = streamReader.readLine()) != null){
                responseStrBuilder.append(inputStr);
            }

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
//            log.info("API Response : {}" , jsonObject.toString());

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

                // 우편번호 추출
                JSONArray ja = jo.getJSONArray("address_components");
                String zip = "";

//                System.out.println("LAT : \t \t" + lat);
//                System.out.println("LNG : \t \t" + lng);


//                for (int l = 0; l < ja.length(); l++) {
//                    JSONObject curjo = ja.getJSONObject(l);
//                    String type = curjo.getJSONArray("types").getString(0);
//                    String short_name = curjo.getString("short_name");
//                    if (type.equals("postal_code")) {
//                        System.out.println("POSTAL_CODE: " + short_name);
//                        ret.put("zip", short_name);
//                    } else if (type.equals("administrative_area_level_3")) {
//                        System.out.println("CITY: " + short_name);
//                        ret.put("city", short_name);
//                    } else if (type.equals("administrative_area_level_2")) {
//                        System.out.println("PROVINCE: " + short_name);
//                        ret.put("province", short_name);
//                    } else if (type.equals("administrative_area_level_1")) {
//                        System.out.println("REGION: " + short_name);
//                        ret.put("region", short_name);
//                    }
//                }


                for (int l = 0; l < ja.length(); l++) {
                    JSONObject curjo = ja.getJSONObject(l);
                    String type = curjo.getJSONArray("types").getString(0);
                    if (type.equals("postal_code")) {
                        zip = curjo.getString("short_name");
                        break; // 우편번호 찾으면 반복 종료
                    }
                }

                ret.put("주소" , clientAddress);
                ret.put("우편 번호", zip);
                ret.put("위도", lat.toString());
                ret.put("경도", lng.toString());

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

    @Transactional
    public void deleteClientsById(int clientId){
        clientRepository.deleteById(clientId);
    }

}
