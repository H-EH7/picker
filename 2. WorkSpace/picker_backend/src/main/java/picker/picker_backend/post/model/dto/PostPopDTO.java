package picker.picker_backend.post.model.dto;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPopDTO {
    long postId;
    int todayViewCount;
    int likesCount;
    int totalViewCount;
    int replyCount;
    Date updatedAt;
}
