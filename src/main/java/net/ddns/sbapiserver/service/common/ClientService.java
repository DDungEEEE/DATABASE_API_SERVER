package net.ddns.sbapiserver.service.common;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.comon.ClientsDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.client.QClients;
import net.ddns.sbapiserver.exception.custom.BusinessException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final JPAQueryFactory jpaQueryFactory;

    public ClientsDto.Result addClients(ClientsDto.Create create){

        if(isClientsDuplicated(create.getClientName())){
            ErrorCode duplicateClientError = ErrorCode.DUPLICATE_USER_ID_ERROR;
            throw new BusinessException(duplicateClientError, duplicateClientError.getReason());
        }

        Clients createClients = create.asEntity();
        Clients saveClients = clientRepository.save(createClients);
        return ClientsDto.Result.of(saveClients);
    }


    public boolean isClientsDuplicated(String clientName){
        QClients qClients = QClients.clients;

        Clients clients = jpaQueryFactory.select(qClients)
                .from(qClients)
                .where(qClients.clientName.eq(clientName))
                .fetchOne();

        return clients != null;
    }
}
