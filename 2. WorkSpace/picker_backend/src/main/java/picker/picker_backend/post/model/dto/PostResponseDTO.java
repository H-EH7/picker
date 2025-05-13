package picker.picker_backend.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String tempId;
    private PostStatus status;
    private PostEventType eventType;
}
