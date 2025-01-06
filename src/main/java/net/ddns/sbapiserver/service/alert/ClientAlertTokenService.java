package net.ddns.sbapiserver.service.alert;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.alert.ClientAlertTokenDto;
import net.ddns.sbapiserver.domain.entity.client.ClientAlertToken;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.repository.client.ClientAlertTokenRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientAlertTokenService {

    private final ClientAlertTokenRepository clientAlertTokenRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    @Transactional
    public ClientAlertTokenDto.Result createClientAlertToken(ClientAlertTokenDto.Create create){
        Clients findClient = serviceErrorHelper.findClientsOrElseThrow404(create.getClientId());

        ClientAlertToken createAlertToken = create.asEntity(client -> client.withClients(findClient));
        ClientAlertToken saveAlertToken = clientAlertTokenRepository.save(createAlertToken);

        return ClientAlertTokenDto.Result.of(saveAlertToken);
    }

    @Transactional(readOnly = true)
    public List<ClientAlertTokenDto.Result> findClientAlertTokenByClientId(int clientId){
        serviceErrorHelper.findClientsOrElseThrow404(clientId);

        List<ClientAlertToken> findClientAlertTokens = clientAlertTokenRepository.getAllByClientsClientId(clientId);
        return ClientAlertTokenDto.Result.of(findClientAlertTokens);
    }

    @Transactional(readOnly = true)
    public List<ClientAlertTokenDto.Result> getAllClientAlertTokens(){
        return ClientAlertTokenDto.Result.of(clientAlertTokenRepository.findAll());
    }


    @Transactional
    public void deleteClientAlertToken(ClientAlertTokenDto.Delete delete){
        serviceErrorHelper.findClientsOrElseThrow404(delete.getClientId());

        clientAlertTokenRepository.deleteByClientsClientIdAndTokenNumber(delete.getClientId(), delete.getTokenNumber());
    }
}
