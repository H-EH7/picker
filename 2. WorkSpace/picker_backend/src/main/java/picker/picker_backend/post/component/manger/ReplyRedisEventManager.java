package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.postenum.EventType;

@Slf4j
@Component
@AllArgsConstructor
public class ReplyRedisEventManager implements RedisEventManger {

    private final PostRedisReplyCountManager postRedisReplyCountManager;

    @Override
    public void eventMange(EventType eventType, Long postId) {

        switch (eventType){
            case INSERT -> postRedisReplyCountManager.incrementReplyCount(postId);
            case DELETE -> postRedisReplyCountManager.deleteReplyCount(postId);
            default -> {}
        }
    }
}
