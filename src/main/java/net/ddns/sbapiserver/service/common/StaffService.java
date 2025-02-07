package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.dto.StaffPasswordEditDto;
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

    @Transactional
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

    @Transactional(readOnly = true)
    public List<StaffDto.Result> getAllStaffs(){
        return StaffDto.Result.of(staffRepository.findAll());
    }


    @Transactional
    public StaffDto.Result updateStaff(StaffDto.Put put){
        Staffs findStaff = serviceErrorHelper.findStaffOrElseThrow404(put.getStaffId());
        Staffs putEntity = put.asPutEntity(findStaff);
        Staffs saveEntity = staffRepository.save(putEntity);

        return StaffDto.Result.of(saveEntity);
    }

    @Transactional
    public StaffDto.Result editStaffPassword(StaffPasswordEditDto passwordEditDto){
        Staffs findStaffs = serviceErrorHelper.findStaffOrElseThrow404(passwordEditDto.getStaffId());
        findStaffs.setStaffPassword(passwordEncoder.encode(passwordEditDto.getStaffPassword()));
        Staffs saveStaffs = staffRepository.save(findStaffs);
        return StaffDto.Result.of(saveStaffs);
    }

    @Transactional
    public void deleteStaff(int staffId) {
        serviceErrorHelper.findStaffOrElseThrow404(staffId);
        noticeRepository.deleteAllByStaffsStaffId(staffId);
        staffRepository.deleteById(staffId);
    }
}


