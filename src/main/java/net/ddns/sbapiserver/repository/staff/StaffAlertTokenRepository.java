package net.ddns.sbapiserver.repository.staff;

import net.ddns.sbapiserver.domain.entity.staff.StaffAlertToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffAlertTokenRepository extends JpaRepository<StaffAlertToken, Integer> {

    void deleteByStaffsStaffIdAndTokenNumber(int staffId, int tokenNumber);
    List<StaffAlertToken> getAllByStaffsStaffId(int staffId);
}
