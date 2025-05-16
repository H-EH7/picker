package picker.picker_backend.post.reply.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.redis.PostRedisService;
import picker.picker_backend.post.reply.model.dto.*;
import picker.picker_backend.post.reply.service.ReplyClientService;
import picker.picker_backend.post.reply.service.ReplyQueryService;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/reply")
@RequiredArgsConstructor
public class ReplyController {

    @Autowired
    private ReplyClientService replyClientService;

    @Autowired
    private ReplyQueryService replyQueryService;

    @Autowired
    private PostRedisService postRedisService;

    @GetMapping
    public ResponseEntity<PostApiResponseWrapper<List<ReplySelectDTO>>> getReply(@PathVariable Long postId){

        return replyQueryService.getReply(postId);
    }

    @GetMapping("/status")
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> getReplyStatus(@RequestParam String tempId,
                                                                                  @RequestParam String eventType){
        return postRedisService.getReplyStatus(tempId,eventType);
    }

    @PostMapping("/insert")
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertReply(@PathVariable Long postId,
                                                                               @RequestParam String userId,
                                                                               @RequestParam String replyText,
                                                                               @RequestParam String tempId,
                                                                               @RequestParam(required = false) Long parentReplyId){
        return replyClientService.insertReply(
                ReplyInsertRequestDTO.builder()
                        .postId(postId)
                        .userId(userId)
                        .replyText(replyText)
                        .tempId(tempId)
                        .parentReplyId(parentReplyId)
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> updateReply(@PathVariable Long postId,
                                                                               @RequestParam String userId,
                                                                               @RequestParam String replyText,
                                                                               @RequestParam String tempId,
                                                                               @RequestParam long replyId){
        return replyClientService.updateReply(
                ReplyUpdateRequestDTO.builder()
                        .replyId(replyId)
                        .postId(postId)
                        .userId(userId)
                        .replyText(replyText)
                        .tempId(tempId)
                        .build()
        );
    }

    @PutMapping("/delete")
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> deleteReply(@PathVariable Long postId,
                                                                               @RequestParam String userId,
                                                                               @RequestParam String tempId,
                                                                               @RequestParam long replyId){
        return replyClientService.deleteReply(
                ReplyDeleteRequestDTO.builder()
                        .replyId(replyId)
                        .postId(postId)
                        .userId(userId)
                        .tempId(tempId)
                        .build()
        );
    }

}
