package picker.picker_backend.post.likes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikesSelectDTO {
    private String userId;
    private long postId;
}
