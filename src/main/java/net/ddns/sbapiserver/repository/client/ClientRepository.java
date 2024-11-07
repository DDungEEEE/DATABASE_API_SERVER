package net.ddns.sbapiserver.repository.client;

import net.ddns.sbapiserver.domain.entity.client.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Clients, Integer> {
}
