package picker.picker_backend.post.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class PostEntity {
    private String userId;
    private Long postId;
    private String postText;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;
    private String tempId;
    private String filePath;
    private int viewCount;
    private int replyCount;
    private int likesCount;
}
