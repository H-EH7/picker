package picker.picker_backend.post.likes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picker.picker_backend.post.model.common.UserIdSupport;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikesInsertRequestDTO implements UserIdSupport {
    private String userId;
    private Long postId;

    @Override
    public String getUserId() {
        return userId;
    }
}
