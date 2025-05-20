package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class PostRedisReplyCountManager {
    private final RedisTemplate<String, String> redisTemplate;

    @Async("postReplyExecutor")
    public void setReplyCount(long postId) {
        redisTemplate.opsForZSet().add("post:replycount", String.valueOf(postId), 0);
    }

    public int getReplyCount(long postId) {
        Double viewCount =  redisTemplate.opsForZSet().score("post:replycount", postId);
        return viewCount != null ? (int)viewCount.doubleValue() : -1;
    }

    @Async("postReplyExecutor")
    public void incrementReplyCount(long postId) {
        redisTemplate.opsForZSet().incrementScore("post:replycount", String.valueOf(postId), 1);
    }

    public Map<Object, Object> getAllPostReplyCount() {
        Map<Object, Object> resultPostReplyCount = new LinkedHashMap<>();
        Set<ZSetOperations.TypedTuple<String>> allPostReplyCount=
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores("post:replycount", 0, -1);

        if(allPostReplyCount != null){
            for(ZSetOperations.TypedTuple<String> tuple : allPostReplyCount) {
                resultPostReplyCount.put(tuple.getValue(), tuple.getScore());
            }
        }
        return resultPostReplyCount;
    }

    @Async("postReplyExecutor")
    public void deleteReplyCount(long postId){
        redisTemplate.opsForZSet().incrementScore("post:replycount", String.valueOf(postId), -1);
    }

    @Async("postReplyExecutor")
    public void removeReplyCount(long postId){
        redisTemplate.opsForZSet().remove("post:replycount",String.valueOf(postId));
    }

    public Map<Object, Object> getTopPostReplyCount() {
        Map<Object, Object> resultPostReplyCount = new LinkedHashMap<>();
        Set<ZSetOperations.TypedTuple<String>> topPostReplyCount=
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores("post:replycount", 0, 999);

        if(topPostReplyCount != null){
            for(ZSetOperations.TypedTuple<String> tuple : topPostReplyCount) {
                resultPostReplyCount.put(tuple.getValue(), tuple.getScore());
            }
        }
        return resultPostReplyCount;
    }

}
