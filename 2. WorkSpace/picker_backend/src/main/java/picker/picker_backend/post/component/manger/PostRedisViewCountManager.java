package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component
public class PostRedisViewCountManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRedisReplyCountManager postRedisReplyCountManager;

    @Async("postRedisViewCountExecutor")
    public void setViewCount(long postId) {
        redisTemplate.opsForHash().put("post:viewcount", String.valueOf(postId), "0");
        postRedisReplyCountManager.setReplyCount(postId);
    }

    public int getViewCount(long postId) {
        String viewCount = (String) redisTemplate.opsForHash().get("post:viewcount", postId);
        return viewCount != null ? Integer.parseInt(viewCount) : -1;
    }

    @Async("postRedisViewCountExecutor")
    public void incrementViewCount(long postId) {
        redisTemplate.opsForHash().increment("post:viewcount", String.valueOf(postId), 1);
    }

    public Map<Object, Object> getAllPostViewCount() {
        return redisTemplate.opsForHash().entries("post:viewcount");
    }

    @Async("postRedisViewCountExecutor")
    public void deleteViewCount(long postId){
        redisTemplate.opsForHash().delete("post:viewcount", String.valueOf(postId));
        redisTemplate.opsForHash().delete("post:replycount",String.valueOf(postId));
    }

    @Async("postRedisViewCountExecutor")
    public void initViewCount(){
        Map<String, String> updateAllViewCounts = new HashMap<>();
        redisTemplate.opsForHash().entries("post:viewcount").forEach((key,value)-> updateAllViewCounts.put((String) key, "0"));

        redisTemplate.opsForHash().putAll("post:viewcount", updateAllViewCounts);
    }
}
