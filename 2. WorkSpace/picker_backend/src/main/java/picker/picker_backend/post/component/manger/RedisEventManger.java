package picker.picker_backend.post.component.manger;

import picker.picker_backend.post.postenum.EventType;

public interface RedisEventManger {
    void eventMange(EventType eventType, Long entityId);
}
