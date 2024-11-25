package net.ddns.sbapiserver.service;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffDto.Result createStaff(StaffDto.Create create){
        Staffs createStaff = create.asEntity();

        createStaff.setStaffPassword(passwordEncoder.encode(create.getStaffPassword()));

        Staffs saveStaff = staffRepository.save(createStaff);
        return StaffDto.Result.of(saveStaff);
    }

    public void deleteStaff(int staffId){
        staffRepository.deleteById(staffId);
    }

//    public Staffs findStaffOrThrow404(int staffId){
//        staffRepository.findById(staffId).orElseThrow(new ResponseStatusException())
//
//    }
}
