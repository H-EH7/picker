package picker.picker_backend.post.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import picker.picker_backend.post.model.entity.PostEntity;

@Getter
@NoArgsConstructor
public class PostInsertDTO {
    private String userId;
    private String postText;
    private MultipartFile file;

    public PostInsertDTO(String userId, String postText) {
        this.userId = userId;
        this. postText = postText;
        this.file = file;
    }

    public PostEntity toEntity(){
        PostEntity postEntity = new PostEntity();
        postEntity.setPostText(postText);
        postEntity.setUserId(userId);
        return postEntity;
    }
}
