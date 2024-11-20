package net.ddns.sbapiserver.exception.admin;

import net.ddns.sbapiserver.domain.entity.staff.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {

}
