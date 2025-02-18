package net.ddns.sbapiserver.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.security.UserType;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtToken {
    private String accessToken;
    private String role;
    private Object user;
    // Staffs , Clients 둘 중 하나 가능
}
