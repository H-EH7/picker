package picker.picker_backend.post.likes.service;

import org.springframework.http.ResponseEntity;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.likes.model.dto.LikesSelectDTO;

import java.util.List;

public interface LikesQueryService {

   ResponseEntity<PostApiResponseWrapper<List<LikesSelectDTO>>> getLikesByUserId(String userId);

}
