package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.client.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Integer> {

}
