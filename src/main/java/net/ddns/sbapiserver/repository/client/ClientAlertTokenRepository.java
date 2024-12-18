package net.ddns.sbapiserver.repository.client;

import net.ddns.sbapiserver.domain.entity.client.ClientAlertToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientAlertTokenRepository extends JpaRepository<ClientAlertToken, Integer> {

    void deleteByClientsClientIdAndTokenNumber(int clientId, int tokenNumber);
    List<ClientAlertToken> getAllByClientsClientId(int clientId);
}
