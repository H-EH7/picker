package picker.picker_backend.post.model.dto;

import lombok.*;
import picker.picker_backend.post.annotation.PostAnnotation;
import picker.picker_backend.post.model.common.TempIdSupport;
import picker.picker_backend.post.model.common.UserIdSupport;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PostAnnotation(topicKey = TopicKey.POST, eventType = EventType.INSERT)
public class PostInsertRequestDTO implements TempIdSupport, UserIdSupport {
    private String userId;
    private String postText;
    private String tempId;
    private long potId;

    @Override
    public String getTempId(){
        return tempId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

}
