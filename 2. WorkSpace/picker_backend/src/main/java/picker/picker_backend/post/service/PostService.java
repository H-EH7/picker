package picker.picker_backend.post.service;

import picker.picker_backend.post.model.dto.PostDTO;

public interface PostService {

    PostDTO getPostById(String userId);
}
