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
import java.util.Comparator;
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
        List<Notice> allNoticeList = noticeRepository.findAll();
        List<Notice> sortedByNoticeDate = sortedByNoticeDate(allNoticeList);
        return NoticeDto.Result.of(sortedByNoticeDate);
    }

    @Transactional
    public NoticeDto.Result updateNotice(NoticeDto.Put put){
        Notice notice = serviceErrorHelper.findNoticeOrElseThrow404(put.getNoticeId());
        Staffs staffOrElseThrow404 = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());

        Notice putEntity = put.asPutEntity(notice.withStaffs(staffOrElseThrow404));
        return NoticeDto.Result.of(noticeRepository.save(putEntity));

    }

    @Transactional(readOnly = true)
    public NoticeDto.Result findNoticeById(int noticeId){
        return NoticeDto.Result.of(serviceErrorHelper.findNoticeOrElseThrow404(noticeId));
    }

    @Transactional(readOnly = true)
    public List<NoticeDto.Result> searchNotice(LocalDate startDate, LocalDate endDate){
        QNotice notice = QNotice.notice;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Notice> searchNoticeList = jpaQueryFactory.selectFrom(notice)
                .where(notice.noticeDate.between(startDateTime, endDateTime))
                .fetch();

        List<Notice> sortedByNoticeDate = sortedByNoticeDate(searchNoticeList);
        return NoticeDto.Result.of(sortedByNoticeDate);
    }

    @Transactional(readOnly = true)
    public List<NoticeDto.Result> findNotice(int noticeId, int page){
        if(page == 0){
            Notice findNotice = serviceErrorHelper.findNoticeOrElseThrow404(noticeId);
            return List.of(NoticeDto.Result.of(findNotice));
        }else{
            Pageable pageable = PageRequest.of(page-1, 10, Sort.by("noticeId").ascending());
            Page<Notice> findNotices = noticeRepository.findNoticesByAfterId(noticeId, pageable);
            List<Notice> sortedByNoticeDate = sortedByNoticeDate(findNotices.getContent());

            return NoticeDto.Result.of(sortedByNoticeDate);
        }
    }

    @Transactional
    public void deleteNotice(int noticeId){
        serviceErrorHelper.findNoticeOrElseThrow404(noticeId);
        noticeRepository.deleteById(noticeId);
    }

    protected List<Notice> sortedByNoticeDate(List<Notice> noticeList){
       noticeList.sort(Comparator.comparing(Notice::getNoticeDate).reversed());
       return noticeList;
    }
}
