package net.ddns.sbapiserver.service.alert;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.alert.StaffAlertTokenDto;
import net.ddns.sbapiserver.domain.entity.staff.StaffAlertToken;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.staff.StaffAlertTokenRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffAlertTokenService {
    private final StaffAlertTokenRepository staffAlertTokenRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    @Transactional
    public StaffAlertTokenDto.Result createStaffAlertToken(StaffAlertTokenDto.Create create){
        Staffs findStaffs = serviceErrorHelper.findStaffOrElseThrow404(create.getStaffId());
        StaffAlertToken createStaffAlertToken = create.asEntity(staffAlertToken -> staffAlertToken.withStaffs(findStaffs));

        StaffAlertToken saveStaffAlertToken = staffAlertTokenRepository.save(createStaffAlertToken);
        return StaffAlertTokenDto.Result.of(saveStaffAlertToken);
    }

    @Transactional(readOnly = true)
    public List<StaffAlertTokenDto.Result> findStaffAlertTokensByStaffId(int staffId){
        serviceErrorHelper.findStaffOrElseThrow404(staffId);

        List<StaffAlertToken> findStaffAlertTokens = staffAlertTokenRepository.getAllByStaffsStaffId(staffId);
        return StaffAlertTokenDto.Result.of(findStaffAlertTokens);
    }

    @Transactional
    public void deleteStaffAlertToken(StaffAlertTokenDto.Delete delete){
        serviceErrorHelper.findStaffOrElseThrow404(delete.getStaffId());

        staffAlertTokenRepository.deleteByStaffsStaffIdAndTokenNumber(delete.getTokenNumber(), delete.getStaffId());
    }
}
