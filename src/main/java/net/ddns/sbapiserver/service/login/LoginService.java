package net.ddns.sbapiserver.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60;
    private static final int EXPIRED_TOKEN_TIME = 0;

    public void handleConcurrentLogin(String userId, String accessToken){
        redisTemplate.opsForValue().get(userId)
    }

    // Redis에 AccessToken 저장
    public void storeAccessToken(String userId, String token){
        log.info("User : [{}] Login", userId);
        redisTemplate.opsForValue().set(userId, token, ACCESS_TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    // AccessToken 삭제
    public void deleteAccessToken(String userId){
        redisTemplate.delete(userId);
    }

    // Login 중 -> true, or false
    public boolean isUserLoggedIn(String userId){
        return redisTemplate.opsForValue().get(userId) != null;
    }

    public void logout(String accessToken, String userId){
        deleteAccessToken(userId);
        log.info("User : [{}] Logout", userId);
    }
}
