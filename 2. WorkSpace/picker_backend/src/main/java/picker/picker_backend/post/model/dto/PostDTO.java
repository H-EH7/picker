package picker.picker_backend.post.model.dto;

import lombok.Getter;

@Getter
public class PostDTO {
    private String userId;

    public PostDTO(String id){
        this.userId = id;
    }

}
