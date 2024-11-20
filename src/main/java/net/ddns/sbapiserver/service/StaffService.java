package net.ddns.sbapiserver.service;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;

    public StaffDto.Result createStaff(StaffDto.Create create){
        Staffs createStaff = create.asEntity();
        Staffs saveStaff = staffRepository.save(createStaff);
        return StaffDto.Result.of(saveStaff);
    }
}
