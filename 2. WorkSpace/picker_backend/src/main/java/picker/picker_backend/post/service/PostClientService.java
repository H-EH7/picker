package picker.picker_backend.post.service;

import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;

public interface PostClientService {

    boolean insertPost(PostInsertDTO postInsertDTO);
    boolean updatePost(PostUpdateDTO postUpdateDTO);
}
