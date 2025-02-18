package net.ddns.sbapiserver.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(name = "refreshTokenReq")
public class RefreshTokenReq {
    @Schema(name = "client_refresh_token")
    private String clientRefreshToken;

    @Schema(name = "client_username")
    private String clientUsername;
}
