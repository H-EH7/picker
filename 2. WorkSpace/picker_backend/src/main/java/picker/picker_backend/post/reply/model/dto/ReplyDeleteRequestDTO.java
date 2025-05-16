package picker.picker_backend.post.reply.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picker.picker_backend.post.annotation.PostAnnotation;
import picker.picker_backend.post.model.common.TempIdSupport;
import picker.picker_backend.post.model.common.UserIdSupport;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PostAnnotation(topicKey = TopicKey.REPLY, eventType = EventType.DELETE)
public class ReplyDeleteRequestDTO implements TempIdSupport, UserIdSupport {
    private String userId;
    private long postId;
    private long replyId;
    private String tempId;

    @Override
    public String getTempId(){
        return tempId;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
