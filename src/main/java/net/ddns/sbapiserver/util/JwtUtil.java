//package net.ddns.sbapiserver.util;
//
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.crypto.SecretKey;
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtUtil {
//    private static final String authorizationHeader = "Authorization";
//    private static final String authorizationKey = "auth";
//    private static final  String BEARER = "Bearer ";
//    private final Date accessTokenExpired = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
//    private final Date refreshTokenExpired = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//    private Key key;
//    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//    @PostConstruct
//    public void init(){
//        byte[] decode = Base64.getDecoder().decode(secretKey);
//        key = Keys.hmacShaKeyFor(decode);
//    }
//
//    public JwtToken generateToken(Authentication authentication){
//        String authorities = authentication.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority())
//                .collect(Collectors.joining(","));
//
//        generateAccessToken(authentication);
//    }
//
//    public String generateAccessToken(Authentication authentication){
//        String authorities = authentication.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority())
//                .collect(Collectors.joining(","));
//
//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim(authorizationKey, authorities)
//                .setExpiration(accessTokenExpired)
//                .signWith(key, signatureAlgorithm)
//                .compact();
//    }
//
//    public String generateRefreshToken(Authentication authentication){
//        String authorities = authentication.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority())
//                .collect(Collectors.joining(","));
//
//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim(authorizationKey, authorities)
//                .setExpiration(refreshTokenExpired)
//                .signWith(key, signatureAlgorithm)
//                .compact();
//    }
//
//    public String getJwtToken(HttpServletRequest req){
//       String bearerToken = req.getHeader(authorizationHeader);
//       if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)){
//           return bearerToken.substring(7);
//       }
//       return null;
//   }
//
//
//
//}
