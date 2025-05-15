package picker.picker_backend.post.reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostSelectDTO;
import picker.picker_backend.post.model.entity.PostEntity;
import picker.picker_backend.post.reply.mapper.ReplyMapper;
import picker.picker_backend.post.reply.model.dto.ReplySelectDTO;
import picker.picker_backend.post.reply.model.entity.ReplyEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyQueryServiceImpl implements ReplyQueryService{


    private final ReplyMapper replyMapper;

    @Override
    public ResponseEntity<PostApiResponseWrapper<List<ReplySelectDTO>>> getReply(@PathVariable Long postId) {
        try{
            List<ReplyEntity> replyEntityList = replyMapper.getReply(postId);

            if(replyEntityList == null || replyEntityList.isEmpty()){
                return ResponseEntity.ok(PostApiResponseWrapper.success(null, "No Reply"));
            }

            List<ReplySelectDTO> replySelectDTOList = replyEntityList.stream().map(
                    replyEntity -> new ReplySelectDTO(
                            replyEntity.getUserId(),
                            replyEntity.getPostId(),
                            replyEntity.getReplyId(),
                            replyEntity.getParentReplyId(),
                            replyEntity.getReplyText(),
                            replyEntity.getCreatedAt(),
                            replyEntity.getUpdatedAt(),
                            replyEntity.getTempId()
                    )).toList();

            return ResponseEntity.ok(
                    PostApiResponseWrapper.success(replySelectDTOList, "Select Success")
            );

        } catch (Exception e) {

            log.error("Error",e);

            throw e;

        }
    }

}
