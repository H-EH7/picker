package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.postenum.EventType;

@Component
@AllArgsConstructor
public class PostRedisEventManger implements RedisEventManger {

    private final PostRedisViewCountManager redisViewCountManager;

    @Override
    public void eventMange(EventType eventType, Long postId) {

        switch (eventType){
            case INSERT -> redisViewCountManager.setViewCount(postId);
            case DELETE -> redisViewCountManager.deleteViewCount(postId);
            default -> {}
        }
    }
}
