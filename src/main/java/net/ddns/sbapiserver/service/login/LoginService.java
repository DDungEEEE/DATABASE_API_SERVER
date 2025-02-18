package net.ddns.sbapiserver.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.security.UserType;
import net.ddns.sbapiserver.service.common.ClientService;
import net.ddns.sbapiserver.service.common.StaffService;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ClientRepository clientRepository;
    private static final int ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60;

    @Transactional
    // Redis에 AccessToken 저장, userId -> 사용자 로그인 시 사용한 id
    public void storeAccessToken(String userId, String token, String role){
        log.info("User : [{}], Role : [{}] Login", role, userId);
        if(role.equals(UserType.CLIENT.getRole())){
            redisTemplate.opsForValue().set(userId, token, ACCESS_TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS);
            updateClientLoginTime(userId);
        }else if(role.equals(UserType.STAFF.getRole())){
            redisTemplate.opsForValue().set(userId, token);
        }
    }

    // AccessToken 삭제
    public void deleteAccessToken(String userId){
        redisTemplate.delete(userId);
    }


    // 사용자가 요청할 시 로그인이 유효한지 검증하는 method
    public boolean isUserLoginValid(String userid, String accessToken){
        String findToken = redisTemplate.opsForValue().get(userid);
        log.error("찾은 Token : {} 입력한 Token : {}", findToken, accessToken);
        if(findToken == null){
            return false;
        }
        return findToken.equals(accessToken);
    }

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


    // client 로그인 한 시간 업데이트
    public void updateClientLoginTime(String clientUserId){
        Clients findClient = clientRepository.findClientsByClientName(clientUserId);
        findClient.setClientLoginTime(new Timestamp(System.currentTimeMillis()));
        clientRepository.save(findClient);
    }

}
