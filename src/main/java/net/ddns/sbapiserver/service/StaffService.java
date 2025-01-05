package net.ddns.sbapiserver.service;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.repository.staff.NoticeRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final NoticeRepository noticeRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    public StaffDto.Result createStaff(StaffDto.Create create) {
        boolean userIdDuplicated = serviceErrorHelper.isUserIdDuplicated(create.getStaffUserId());
        if(userIdDuplicated){
            ErrorCode duplicateUserIdError = ErrorCode.DUPLICATE_USER_ID_ERROR;
            throw new BusinessException(duplicateUserIdError, duplicateUserIdError.getReason());
        }

        Staffs createStaff = create.asEntity();
        createStaff.setStaffPassword(passwordEncoder.encode(create.getStaffPassword()));

        Staffs saveStaff = staffRepository.save(createStaff);
        return StaffDto.Result.of(saveStaff);
    }

    public List<StaffDto.Result> getAllStaffs(){
        return staffRepository.findAll().stream().map(StaffDto.Result::of).collect(Collectors.toList());
    }

    public StaffDto.Result updateStaff(StaffDto.Put put){
        Staffs findStaff = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());
        Staffs putEntity = put.asPutEntity(findStaff);
        Staffs saveEntity = staffRepository.save(putEntity);

        return StaffDto.Result.of(saveEntity);
    }

    @Transactional
    public void deleteStaff(int staffId) {
        serviceErrorHelper.findStaffOrElseThrow404(staffId);
        noticeRepository.deleteAllByStaffsStaffId(staffId);
        staffRepository.deleteById(staffId);
    }
}


