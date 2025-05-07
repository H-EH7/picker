package picker.picker_backend.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateDTO {
    private long postId;
    private String userId;
    private String postText;
    private String filepath;
}
