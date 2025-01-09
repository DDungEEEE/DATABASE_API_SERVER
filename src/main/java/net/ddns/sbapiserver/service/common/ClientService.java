package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.ClientPasswordEditDto;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final PasswordEncoder passwordEncoder;

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
        putClients.setClientPassword(passwordEncoder.encode(putClients.getClientPassword()));

        Clients saveClients = clientRepository.save(putClients);

        return saveClients;

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
