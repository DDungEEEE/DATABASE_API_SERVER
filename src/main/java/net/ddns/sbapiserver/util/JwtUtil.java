package net.ddns.sbapiserver.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {
    private static final String authorizationHeader = "Authorization";
    private static final String authorizationKey = "auth";
    private static final  String BEARER = "Bearer ";
    private final Date accessTokenExpired = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
    private final Date refreshTokenExpired = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] decode = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decode);
    }

    public JwtToken generateToken(String userId, String role){
        return JwtToken.builder()
                .accessToken(generateAccessToken(userId, role))
                .role(role)
                .build();
    }

    public boolean validToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.error("Invalid Jwt Signature, 유효허지 않은 Jwt 서명입니다.");
        }catch (ExpiredJwtException e){
            log.error("유효기간이 만료된 Jwt Token 입니다.");
        }catch (UnsupportedJwtException e){
            log.error("지원하지 않는 Jwt Token 입니다.");
        }catch (IllegalArgumentException e){
            log.error("Jwt claims 가 비어있쓰빈다.");
        }
        return false;
    }

    public String generateAccessToken(String userId, String role){

        return Jwts.builder()
                .setSubject(userId)
                .claim(authorizationKey, role)
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpired)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String generateRefreshToken(String userId){
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(refreshTokenExpired)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String getJwtToken(HttpServletRequest req){
       String bearerToken = req.getHeader(authorizationHeader);
       if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)){
           return bearerToken.substring(7);
       }
       return null;
   }

   public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
   }




}
