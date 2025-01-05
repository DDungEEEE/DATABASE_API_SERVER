package net.ddns.sbapiserver.repository.staff;

import net.ddns.sbapiserver.domain.entity.staff.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    void deleteAllByStaffsStaffId(int staffId);
    @Query("SELECT n FROM Notice n where n.noticeId >= :noticeId")
    Page<Notice> findNoticesByAfterId(@Param("noticeId") Integer noticeId, Pageable pageable);
}
