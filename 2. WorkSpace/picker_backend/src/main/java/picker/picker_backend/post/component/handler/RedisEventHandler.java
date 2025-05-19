package picker.picker_backend.post.component.handler;

import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.*;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

import java.util.Map;

@Component
public class RedisEventHandler {
    private final Map<TopicKey, RedisEventManger> handlerMap;

    public RedisEventHandler(PostRedisEventManger postRedisEventManger, ReplyRedisEventManager replyRedisEventManager){
        handlerMap = Map.of(
                TopicKey.POST, postRedisEventManger,
                TopicKey.REPLY, replyRedisEventManager
        );
    }

    public void handler(TopicKey topicKey, EventType eventType, Long entityId){
        RedisEventManger redisEventManger = handlerMap.get(topicKey);
        if(redisEventManger !=null){
            redisEventManger.eventMange(eventType,entityId);
        }
    }
}
