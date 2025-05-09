package picker.picker_backend.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSelectDTO {
    private String userId;
    private long postId;
    private String postText;
    private Date createdAt;
    private Date updatedAt;

}
