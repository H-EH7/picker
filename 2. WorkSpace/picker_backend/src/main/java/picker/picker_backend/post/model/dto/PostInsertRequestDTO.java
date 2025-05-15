package picker.picker_backend.post.model.dto;

import lombok.*;
import picker.picker_backend.post.model.common.TempIdSupport;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostInsertRequestDTO implements TempIdSupport {
    private String userId;
    private String postText;
    private String tempId;

    @Override
    public String getTempId(){
        return tempId;
    }

}
