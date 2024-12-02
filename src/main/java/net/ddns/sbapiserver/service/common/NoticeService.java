package net.ddns.sbapiserver.service.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.staff.NoticeDto;
import net.ddns.sbapiserver.domain.entity.staff.Notice;
import net.ddns.sbapiserver.domain.entity.staff.QNotice;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.staff.NoticeRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public NoticeDto.Result createNotice(NoticeDto.Create create){
        Staffs staffs = serviceErrorHelper.findStaffOrElseThrow404(create.getStaffId());

        Notice requestNotice = create.asEntity(notice -> notice.withStaffs(staffs));
        Notice createNotice = noticeRepository.save(requestNotice);
        return NoticeDto.Result.of(createNotice);
    }

    public List<NoticeDto.Result> getNoticeList(){
        return NoticeDto.Result.of(noticeRepository.findAll());
    }

    @Transactional
    public NoticeDto.Result updateNotice(NoticeDto.Put put){
        Notice notice = serviceErrorHelper.findNoticeOrElseThrow404(put.getNoticeId());
        Staffs staffOrElseThrow404 = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());

        Notice putEntity = put.asPutEntity(notice.withStaffs(staffOrElseThrow404));
        return NoticeDto.Result.of(noticeRepository.save(putEntity));

    }

    public List<NoticeDto.Result> searchNotice(LocalDate startDate, LocalDate endDate){
        QNotice notice = QNotice.notice;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        List<Notice> searchNoticeList = jpaQueryFactory.selectFrom(notice)
                .where(notice.noticeDate.between(startDateTime, endDateTime))
                .fetch();
        return NoticeDto.Result.of(searchNoticeList);
    }

    @Transactional
    public void deleteNotice(int noticeId){
        serviceErrorHelper.findNoticeOrElseThrow404(noticeId);
        noticeRepository.deleteById(noticeId);
    }
}
