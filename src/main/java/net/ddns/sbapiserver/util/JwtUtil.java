package net.ddns.sbapiserver.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.security.UserType;
import net.ddns.sbapiserver.security.dto.JwtToken;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    private static final String authorizationHeader = "Authorization";
    private static final String authorizationKey = "auth";
    private static final  String BEARER = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRED =  30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRED = 24 * 60 * 60 * 1000L;

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] decode = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decode);
    }

    //Client에게 전송할 JwtToken 객체 생성
    public JwtToken generateToken(String userId, String role){
        if(role.equals(UserType.STAFF.getRole())){
            return JwtToken.builder()
                    .accessToken(generateStaffAccessToken(userId, role))
                    .role(role)
                    .build();
        }else if(role.equals(UserType.CLIENT.getRole())){
            return JwtToken.builder()
                    .accessToken(generateAccessToken(userId, role))
                    .role(role)
                    .build();
        }else{
            return null;
        }
    }

    public boolean validToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.error("Invalid Jwt Signature, 유효허지 않은 Jwt 서명입니다.");
        }catch (ExpiredJwtException e){
            log.error(e.getMessage());
            log.error("유효기간이 만료된 Jwt Token 입니다.");
        }catch (UnsupportedJwtException e){
            log.error("지원하지 않는 Jwt Token 입니다.");
        }catch (IllegalArgumentException e){
            log.error("{}", e.getMessage());
        }
        return false;
    }

    public String generateStaffAccessToken(String userId, String role){
        log.info("{} --------------------- Staff AccessToken generated", userId);
        return Jwts.builder()
                .setSubject(userId)
                .claim(authorizationKey, role)
                .setIssuedAt(new Date())
                .signWith(key, signatureAlgorithm)
                .compact();
    }
    public String generateAccessToken(String userId, String role){
        log.info("{} ------------ Client AccessToken Generation", userId);
        Date date = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .claim(authorizationKey, role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRED))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // userId로 RefreshToken 생성
    public String generateRefreshToken(String userId){
        log.info("{} ------------ Generated New RefreshToken", userId);
        Date date = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRED))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // req Header 에서 jwt Token 추출
    public String getJwtToken(HttpServletRequest req){
       String bearerToken = req.getHeader(authorizationHeader);
       if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)){
           return bearerToken.substring(7);
       }
       return null;
   }

   public Claims getClaims(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            log.info("token 재발급 요청");
            return e.getClaims();
        }
   }

   public JwtToken generateAcTokenByReToken(String refreshToken){
       Claims claims = getClaims(refreshToken);
       String clientUserName = claims.getSubject();
       return generateToken(clientUserName, UserType.CLIENT.getRole());
   }

   public String extractRole(String token){
       Claims claims = getClaims(token);
       return claims.get(authorizationKey, String.class);
   }

}
