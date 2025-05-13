package picker.picker_backend.post.model.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateRequestDTO {
    private long postId;
    private String userId;
    private String postText;
    private String tempId;
}
