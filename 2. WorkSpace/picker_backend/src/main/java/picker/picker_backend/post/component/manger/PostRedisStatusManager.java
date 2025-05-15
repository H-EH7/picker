package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Component
public class PostRedisStatusManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;

    private String getPostRedisTempIdKey(EventType eventType, String tempId, String topicName){
        String postEventType = eventType.name();

        return getTopic(topicName) + ":" + postEventType + ":" + tempId;
    }

    private String getTopic(String topicName){
        return postTopicKeyMapperHelper.getTopicKey(topicName).name();
    }

    @Async("postRedisStatusExecutor")
    public void setStatus(EventType eventType, String tempId, Status status, String topicName){

        redisTemplate.opsForHash().put(getPostRedisTempIdKey(eventType,tempId, topicName), "status", status.name());
    }

    @Async("postRedisStatusExecutor")
    public void setStatusWithTimestamp(EventType eventType, String tempId, Status status, String topicName){
        try{
            redisTemplate.opsForHash().put(getPostRedisTempIdKey(eventType,tempId, topicName), "status", status.name());
            redisTemplate.opsForHash().put(
                    getPostRedisTempIdKey(eventType,tempId,topicName),
                    "updateAt",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );

            if(status == Status.SUCCESS){
                setExpireTime(eventType, tempId, topicName);
            }

        }catch (Exception e){
            log.error("Post Redis Status Error ", e);
        }
    }

    public Status getStatus(EventType eventType, String tempId,String topicName){
        String status = (String) redisTemplate.opsForHash().get(getPostRedisTempIdKey(eventType,tempId, topicName), "status");
        return status != null ? Status.valueOf(status) : null;
    }

    @Async("postRedisStatusExecutor")
    public void setStatusMap(EventType eventType, String tempId, Map<String, String> postStatusMap, TopicKey topicKey){
        String topicName = postTopicKeyMapperHelper.getTopicName(topicKey);
        redisTemplate.opsForHash().putAll(getPostRedisTempIdKey(eventType, tempId,topicName), postStatusMap);
    }

    public Map<Object, Object> getPostStatusMap(EventType eventType, String tempId, String topicName){
        return redisTemplate.opsForHash().entries(getPostRedisTempIdKey(eventType,tempId,topicName));
    }

    public void setExpireTime(EventType eventType, String tempId,String topicName){
        redisTemplate.expire(getPostRedisTempIdKey(eventType, tempId, topicName),24, TimeUnit.HOURS );
    }

}
