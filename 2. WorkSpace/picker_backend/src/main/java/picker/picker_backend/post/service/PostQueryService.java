package picker.picker_backend.post.service;

import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostSelectDTO;

import java.util.List;

public interface PostQueryService {

    PostApiResponseWrapper<List<PostSelectDTO>> getPostById(String userId);

}
