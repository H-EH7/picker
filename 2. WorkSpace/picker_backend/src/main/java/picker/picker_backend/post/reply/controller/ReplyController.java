package picker.picker_backend.post.reply.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.model.dto.PostSelectDTO;
import picker.picker_backend.post.reply.model.dto.ReplyResponseDTO;
import picker.picker_backend.post.reply.model.dto.ReplySelectDTO;
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

    @GetMapping
    public ResponseEntity<PostApiResponseWrapper<List<ReplySelectDTO>>> getReply(@PathVariable Long postId){

        return replyQueryService.getReply(postId);
    }

    @GetMapping("/insert")
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertReply(@PathVariable Long postId,
                                                                               @RequestParam String userId,
                                                                               @RequestParam String replyText){

        return null;
    }
}
