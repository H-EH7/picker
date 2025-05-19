package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class LikesRedisManger {

    private final RedisTemplate<String, String> redisTemplate;

    public String getLikeRedisKey(Long postId){

        return "post:likes:" + postId;
    }

    @Async("likesExecutor")
    public void addLikes(Long postId, String userId){
        redisTemplate.opsForSet().add(getLikeRedisKey(postId), userId);
    }

    @Async("likesExecutor")
    public void removeLikes(Long postId, String userId){
        redisTemplate.opsForSet().remove(getLikeRedisKey(postId),userId);
    }

    public long getLikeCount(Long postId){
        try {
            Long size = redisTemplate.opsForSet().size(getLikeRedisKey(postId));

            return size != null ? size : 0L
                    ;
        }catch (Exception e){

            return 0L;
        }
    }

}
