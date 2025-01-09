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
@Schema(description = "사용자 로그인 요청 DTO")
public class UserLoginDto {
    @Schema(name = "userId", description = "사용자가 ID", example = "string")
    private String userId;
    @Schema(name = "userPw", description = "사용자 PASSWORD", example = "rhdoddldml!1423")
    private String userPw;
    // 다른 사용자의 로그인을 해제하고 로그인 할것인지
    @Schema(name = "checkSum")
    private int checkSum;
}
