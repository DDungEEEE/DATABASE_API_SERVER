package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.notice.NoticeDto;
import net.ddns.sbapiserver.domain.entity.staff.Notice;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.exception.admin.NoticeRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final StaffRepository staffRepository;

    public NoticeDto.Result createNotice(NoticeDto.Create create){
        Staffs staffs = staffRepository.findById(create.getStaffId()).get();
        Notice requestNotice = create.asEntity(notice -> notice.withStaffs(staffs));
        Notice createNotice = noticeRepository.save(requestNotice);
        return NoticeDto.Result.of(createNotice);
    }

    public List<NoticeDto.Result> getNoticeList(){
        return NoticeDto.Result.of(noticeRepository.findAll());
    }

    public NoticeDto.Result updateNotice(NoticeDto.Put put){
        Notice notice = noticeRepository.findById(put.getNoticeId()).get();
        Notice putEntity = put.asPutEntity(notice);
        return NoticeDto.Result.of(noticeRepository.save(putEntity));

    }
}
