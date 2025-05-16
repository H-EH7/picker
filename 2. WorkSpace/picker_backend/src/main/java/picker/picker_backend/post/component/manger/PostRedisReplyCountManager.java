package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class PostRedisReplyCountManager {
    private final RedisTemplate<String, String> redisTemplate;

    @Async("postRedisReplyCountExecutor")
    public void setReplyCount(long postId) {
        redisTemplate.opsForHash().put("post:replycount", String.valueOf(postId), "0");
    }

    public int getReplyCount(long postId) {
        String viewCount = (String) redisTemplate.opsForHash().get("post:replycount", postId);
        return viewCount != null ? Integer.parseInt(viewCount) : -1;
    }

    @Async("postRedisReplyCountExecutor")
    public void incrementReplyCount(long postId) {
        redisTemplate.opsForHash().increment("post:replycount", String.valueOf(postId), 1);
    }

    public Map<Object, Object> getAllPostReplyCount() {
        return redisTemplate.opsForHash().entries("post:replycount");
    }

    @Async("postRedisReplyCountExecutor")
    public void deleteReplyCount(long postId){
        redisTemplate.opsForHash().increment("post:replycount", String.valueOf(postId), -1);
    }

}
