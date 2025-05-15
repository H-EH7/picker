package picker.picker_backend.post.reply.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ReplyEntity {
    private Long replyId;
    private Long parentReplyId;
    private String userId;
    private Long postId;
    private String replyText;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;
    private String tempId;
}
