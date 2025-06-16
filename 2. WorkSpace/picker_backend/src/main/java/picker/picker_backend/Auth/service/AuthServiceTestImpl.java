package picker.picker_backend.Auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceTestImpl implements AuthService{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveAccessToken(String userId , String token) {
        redisTemplate.opsForValue().set("access:" + userId , token, 1, TimeUnit.HOURS);
    }

    @Override
    public void saveRefreshToken(String userId , String token) {
        redisTemplate.opsForValue().set("refresh:" + userId , token, 7, TimeUnit.DAYS);
    }

    @Override
    public String getToken(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }
}
