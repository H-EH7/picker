package picker.picker_backend.post.service;

import org.springframework.http.ResponseEntity;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostSelectDTO;

import java.util.List;

public interface PostQueryService {

    ResponseEntity<PostApiResponseWrapper<List<PostSelectDTO>>> getPostById(String userId);
    ResponseEntity<PostApiResponseWrapper<List<PostSelectDTO>>> getPostLists();
    ResponseEntity<PostApiResponseWrapper<PostSelectDTO>> getPost(long postId);

}
