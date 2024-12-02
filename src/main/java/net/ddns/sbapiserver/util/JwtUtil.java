package net.ddns.sbapiserver.util;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtil {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_KEY = "auth";
    private static final  String BEARER = "Bearer ";

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] decode = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decode);
    }

   public String getJwtToken(HttpServletRequest req){
       String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
       if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)){
           return bearerToken.substring(7);
       }
       return null;
   }

   public String createToken(String userId)
}
