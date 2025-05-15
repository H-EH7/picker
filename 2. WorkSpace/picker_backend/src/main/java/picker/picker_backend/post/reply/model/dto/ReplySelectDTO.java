package picker.picker_backend.post.reply.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplySelectDTO {
    private String userId;
    private long postId;
    private long replyId;
    private long parentReplyId;
    private String replyText;
    private Date createdAt;
    private Date updatedAt;
    private String tempId;
}
