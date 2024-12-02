package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.exception.custom.BusinessException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final PasswordEncoder passwordEncoder;

    public List<ClientsDto.Result> getClientList(){
        List<Clients> allClients = clientRepository.findAll();
        return ClientsDto.Result.of(allClients);
    }

    public ClientsDto.Result addClients(ClientsDto.Create create){

        if(serviceErrorHelper.isUserIdDuplicated(create.getClientName())){
            ErrorCode duplicateClientError = ErrorCode.DUPLICATE_USER_ID_ERROR;
            throw new BusinessException(duplicateClientError, duplicateClientError.getReason());
        }

        Clients createClients = create.asEntity();
        Clients saveClients = clientRepository.save(createClients);
        return ClientsDto.Result.of(saveClients);
    }

    public Clients updateClients(ClientsDto.Put put){

        Clients findClients = serviceErrorHelper.findClientsOrElseThrow404(put.getClientId());

        Clients putClients = put.asPutEntity(findClients);
        putClients.setClientPassword(passwordEncoder.encode(putClients.getClientPassword()));

        Clients saveClients = clientRepository.save(putClients);

        return saveClients;

    }

    public void deleteClientsById(int clientId){
        clientRepository.deleteById(clientId);
    }

}
