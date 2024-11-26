package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.staff.NoticeDto;
import net.ddns.sbapiserver.domain.entity.staff.Notice;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.staff.NoticeRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ServiceErrorHelper serviceErrorHelper;

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
}
