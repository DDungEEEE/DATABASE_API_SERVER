package net.ddns.sbapiserver.repository.staff;

import net.ddns.sbapiserver.domain.entity.staff.StaffAlertToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffAlertTokenRepository extends JpaRepository<StaffAlertToken, Integer> {
}
