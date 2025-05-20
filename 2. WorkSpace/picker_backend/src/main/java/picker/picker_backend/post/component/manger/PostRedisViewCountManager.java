package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Component
public class PostRedisViewCountManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRedisReplyCountManager postRedisReplyCountManager;

    @Async("postRedisViewCountExecutor")
    public void setViewCount(long postId) {
        redisTemplate.opsForZSet().add("post:viewcount", String.valueOf(postId), 0);
        postRedisReplyCountManager.setReplyCount(postId);
    }

    public int getViewCount(long postId) {
        String viewCount = (String) redisTemplate.opsForHash().get("post:viewcount", postId);
        return viewCount != null ? Integer.parseInt(viewCount) : -1;
    }

    @Async("postRedisViewCountExecutor")
    public void incrementViewCount(long postId) {
        redisTemplate.opsForZSet().incrementScore("post:viewcount", String.valueOf(postId), 1);
    }

    public Map<Object, Object> getAllPostViewCount() {
        Map<Object, Object> resultPostViewCount = new LinkedHashMap<>();
        Set<ZSetOperations.TypedTuple<String>> allPostViewCount=
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores("post:viewcount", 0, -1);

        if(allPostViewCount != null){
            for(ZSetOperations.TypedTuple<String> tuple : allPostViewCount) {
                resultPostViewCount.put(tuple.getValue(), tuple.getScore());
            }
        }
        return resultPostViewCount;
    }

    public Map<Object, Object> getTopPostViewCount() {
        Map<Object, Object> resultPostViewCount = new LinkedHashMap<>();
        Set<ZSetOperations.TypedTuple<String>> topPostViewCount=
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores("post:viewcount", 0, 999);

        if(topPostViewCount != null){
            for(ZSetOperations.TypedTuple<String> tuple : topPostViewCount) {
                resultPostViewCount.put(tuple.getValue(), tuple.getScore());
            }
        }
        return resultPostViewCount;
    }

    @Async("postRedisViewCountExecutor")
    public void deleteViewCount(long postId){
        redisTemplate.opsForZSet().remove("post:viewcount", String.valueOf(postId));
        postRedisReplyCountManager.removeReplyCount(postId);
    }

    @Async("postRedisViewCountExecutor")
    public void initViewCount(){
        Set<String> allViewCountsPostId = redisTemplate.opsForZSet().range("post:viewcount", 0,-1);

        if(allViewCountsPostId != null){
            for(String viewCountsPostId : allViewCountsPostId){
                redisTemplate.opsForZSet().add("post:viewcount", viewCountsPostId,0);
            }
        }
    }


}
