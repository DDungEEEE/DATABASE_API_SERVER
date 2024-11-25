package net.ddns.sbapiserver.repository.staff;

import net.ddns.sbapiserver.domain.entity.staff.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {

}
