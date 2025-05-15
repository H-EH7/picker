package picker.picker_backend.post.reply.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.reply.model.dto.ReplySelectDTO;

import java.util.List;

public interface ReplyQueryService {
    ResponseEntity<PostApiResponseWrapper<List<ReplySelectDTO>>> getReply(@PathVariable Long postId);
}
