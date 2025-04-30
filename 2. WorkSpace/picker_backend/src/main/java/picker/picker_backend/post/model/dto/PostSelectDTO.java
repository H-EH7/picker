package picker.picker_backend.post.model.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Getter
public class PostSelectDTO {
    private String userId;
    private long postId;
    private String postText;
    private Date createdAt;
    private Date updatedAt;


    public PostSelectDTO(String userId, long postId, String postText, Date createdAt, Date updatedAt){
        this.userId = userId;
        this.postId = postId;
        this.postText = postText;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
