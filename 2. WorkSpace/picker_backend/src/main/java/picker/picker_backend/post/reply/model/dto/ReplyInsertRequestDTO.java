package picker.picker_backend.post.reply.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.bytebuddy.implementation.Implementation;
import picker.picker_backend.post.annotation.PostAnnotation;
import picker.picker_backend.post.model.common.TempIdSupport;
import picker.picker_backend.post.model.common.UserIdSupport;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PostAnnotation(topicKey = TopicKey.REPLY, eventType = EventType.INSERT)
public class ReplyInsertRequestDTO implements TempIdSupport, UserIdSupport {
    private String userId;
    private String replyText;
    private String tempId;
    private Long postId;
    private Long parentReplyId;

    @Override
    public String getTempId(){
        return tempId;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
