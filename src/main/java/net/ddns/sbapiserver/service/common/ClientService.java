package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.exception.custom.BusinessException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    public ClientsDto.Result addClients(ClientsDto.Create create){

        if(serviceErrorHelper.isUserIdDuplicated(create.getClientName())){
            ErrorCode duplicateClientError = ErrorCode.DUPLICATE_USER_ID_ERROR;
            throw new BusinessException(duplicateClientError, duplicateClientError.getReason());
        }

        Clients createClients = create.asEntity();
        Clients saveClients = clientRepository.save(createClients);
        return ClientsDto.Result.of(saveClients);
    }


}
