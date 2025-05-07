package picker.picker_backend.post.service;

import picker.picker_backend.post.model.dto.PostSelectDTO;

import java.util.List;

public interface PostQueryService {

    List<PostSelectDTO> getPostById(String userId);

}
