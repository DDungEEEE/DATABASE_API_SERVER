package net.ddns.sbapiserver.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @Schema(name = "userId")
    private String userId;
    @Schema(name = "userPw")
    private String userPw;
}
