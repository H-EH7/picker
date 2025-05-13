package picker.picker_backend.post.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDeleteRequestDTO {
    private String userId;
    private long postId;
    private String tempId;
}
