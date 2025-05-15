package picker.picker_backend.post.reply.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picker.picker_backend.post.model.common.TempIdSupport;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDeleteRequestDTO implements TempIdSupport {
    private String userId;
    private long postId;
    private String tempId;

    @Override
    public String getTempId(){
        return tempId;
    }
}
