package net.ddns.sbapiserver.repository.client;

import net.ddns.sbapiserver.domain.entity.client.ClientAlertToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAlertTokenRepository extends JpaRepository<ClientAlertToken, Integer> {
}
