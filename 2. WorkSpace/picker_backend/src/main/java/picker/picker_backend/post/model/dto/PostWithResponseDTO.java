package picker.picker_backend.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import picker.picker_backend.post.reply.model.dto.ReplySelectDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostWithResponseDTO {
    private PostSelectDTO postData;
    private List<ReplySelectDTO> replyData;
    private long likesCount;
}
