package picker.picker_backend.post.likes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picker.picker_backend.post.annotation.PostAnnotation;
import picker.picker_backend.post.model.common.UserIdSupport;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PostAnnotation(topicKey = TopicKey.LIKES, eventType = EventType.INSERT)
public class LikesInsertRequestDTO implements UserIdSupport {
    private String userId;
    private Long postId;

    @Override
    public String getUserId() {
        return userId;
    }
}
