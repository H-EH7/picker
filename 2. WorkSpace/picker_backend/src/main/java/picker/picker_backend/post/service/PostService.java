package picker.picker_backend.post.service;

import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostSelectDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;

import java.util.List;

public interface PostService {

    List<PostSelectDTO> getPostById(String userId);
    int insertPost(PostInsertDTO postInsertDTO);
    int updatePost(PostUpdateDTO postUpdateDTO);
}
