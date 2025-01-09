package net.ddns.sbapiserver.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60;

    // Redis에 AccessToken 저장
    public void storeAccessToken(String userId, String token){
        log.info("User : [{}] Login", userId);
        redisTemplate.opsForValue().set(userId, token, ACCESS_TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    // AccessToken 삭제
    public void deleteAccessToken(String userId){
        redisTemplate.delete(userId);
    }

    public boolean isUserLoginValid(String userid, String accessToken){
        String findToken = redisTemplate.opsForValue().get(userid);
        log.error("찾은 Token : {} 입력한 Token : {}", findToken, accessToken);
        return findToken.equals(accessToken);
    }

    // Login 중 -> true, or false
    public boolean isUserLoggedIn(String userId){
        return redisTemplate.opsForValue().get(userId) != null;
    }

    public void logout(String userId){
        if(!isUserLoggedIn(userId)){
            throw new BusinessException(ErrorCode.USER_NOT_LOGGED_ERROR, ErrorCode.USER_NOT_LOGGED_ERROR.getReason());
        }
        deleteAccessToken(userId);
        System.out.println(userId);
        log.info("User : [{}] Logout", userId);
    }
}
