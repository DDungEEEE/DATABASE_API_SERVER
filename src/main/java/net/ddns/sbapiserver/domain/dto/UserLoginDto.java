package net.ddns.sbapiserver.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDto {
    @Schema(name = "user_id")
    private String userId;
    @Schema(name = "user_pw")
    private String userPw;
}
