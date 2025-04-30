package picker.picker_backend.post.model.dto;

import org.springframework.web.multipart.MultipartFile;
import picker.picker_backend.post.model.entity.PostEntity;

public class PostUpdateDTO {
    private long postId;
    private String userId;
    private String postText;
    private MultipartFile file;

    public PostUpdateDTO(String userId, String postText, long postId) {
        this.userId = userId;
        this. postText = postText;
        this. postId = postId;
    }

    public PostEntity toEntity(){
        PostEntity postEntity = new PostEntity();
        postEntity.setPostText(postText);
        postEntity.setUserId(userId);
        postEntity.setPostId(postId);
        return postEntity;
    }
}
