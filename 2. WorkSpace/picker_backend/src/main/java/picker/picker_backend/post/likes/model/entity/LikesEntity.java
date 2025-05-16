package picker.picker_backend.post.likes.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikesEntity {
    private String userId;
    private Long postId;
}
