package net.ddns.sbapiserver.repository.staff;

import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staffs, Integer> {
    Staffs findStaffsByStaffUserId(String StaffUserId);
}
