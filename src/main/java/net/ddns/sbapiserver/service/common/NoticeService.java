package net.ddns.sbapiserver.service.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.staff.NoticeDto;
import net.ddns.sbapiserver.domain.entity.staff.Notice;
import net.ddns.sbapiserver.domain.entity.staff.QNotice;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.staff.NoticeRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<NoticeDto.Result> searchNotice(LocalDate startDate, LocalDate endDate){
        QNotice notice = QNotice.notice;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        List<Notice> searchNoticeList = jpaQueryFactory.selectFrom(notice)
                .where(notice.noticeDate.between(startDateTime, endDateTime))
                .fetch();
        return NoticeDto.Result.of(searchNoticeList);
    }

    @Transactional(readOnly = true)
    public List<NoticeDto.Result> findNotice(int noticeId, int page){
        if(page == 0){
            Notice findNotice = serviceErrorHelper.findNoticeOrElseThrow404(noticeId);
            return List.of(NoticeDto.Result.of(findNotice));
        }else{
            Pageable pageable = PageRequest.of(page-1, 10, Sort.by("noticeId").ascending());
            Page<Notice> findNotices = noticeRepository.findNoticesByAfterId(noticeId, pageable);

            return findNotices.stream().map(NoticeDto.Result::of).collect(Collectors.toList());
        }
    }

    @Transactional
    public void deleteNotice(int noticeId){
        serviceErrorHelper.findNoticeOrElseThrow404(noticeId);
        noticeRepository.deleteById(noticeId);
    }
}
