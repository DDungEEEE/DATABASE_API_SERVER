package net.ddns.sbapiserver.util;

import lombok.Builder;
import lombok.Data;
import net.ddns.sbapiserver.security.UserType;

@Data
@Builder
public class JwtToken {
    private String accessToken;
    private String role;
}
