package picker.picker_backend.post.reply.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.reply.model.dto.ReplyInsertRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyResponseDTO;

public interface ReplyClientService {
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertReply(ReplyInsertRequestDTO replyInsertRequestDTO);
}
