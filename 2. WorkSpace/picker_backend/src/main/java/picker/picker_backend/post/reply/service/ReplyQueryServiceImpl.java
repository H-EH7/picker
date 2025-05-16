package picker.picker_backend.post.reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.reply.mapper.ReplyMapper;
import picker.picker_backend.post.reply.model.dto.ReplySelectDTO;
import picker.picker_backend.post.reply.model.entity.ReplyEntity;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyQueryServiceImpl implements ReplyQueryService{


    private final ReplyMapper replyMapper;

    @Override
    public ResponseEntity<PostApiResponseWrapper<List<ReplySelectDTO>>> getReply(Long postId) {
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

    @Override
    public List<ReplySelectDTO> getPostWithResponse(long postId) {

        try{
            List<ReplyEntity> replyEntityList = replyMapper.getReply(postId);

            return replyEntityList.stream().map(
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

        } catch (Exception e) {

            log.error("Error",e);

            throw e;

        }
    }
}
