package picker.picker_backend.post.reply.service;

import org.springframework.http.ResponseEntity;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.reply.model.dto.ReplyDeleteRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyInsertRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyUpdateRequestDTO;

public interface ReplyClientService {
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertReply(ReplyInsertRequestDTO replyInsertRequestDTO);
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> updateReply(ReplyUpdateRequestDTO replyUpdateRequestDTO);
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> deleteReply(ReplyDeleteRequestDTO replyDeleteRequestDTO);

}
