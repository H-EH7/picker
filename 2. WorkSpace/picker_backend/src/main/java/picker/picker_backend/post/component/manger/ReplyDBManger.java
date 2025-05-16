package picker.picker_backend.post.component.manger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import picker.picker_backend.post.component.helper.PostModelMapperHelper;
import picker.picker_backend.post.postenum.TopicKey;
import picker.picker_backend.post.reply.mapper.ReplyMapper;
import picker.picker_backend.post.reply.model.dto.ReplyDeleteRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyInsertRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyUpdateRequestDTO;
import picker.picker_backend.post.reply.model.entity.ReplyEntity;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplyDBManger implements DBManger, TopicKeyProvider{

    @Override
    public TopicKey getTopicKey() {
        return TopicKey.REPLY;
    }

    private final ReplyMapper replyMapper;

    @Override
    public CompletableFuture<?> insert(Object dto) {
        return replyDBInsert((ReplyInsertRequestDTO) dto);
    }
    @Override
    public CompletableFuture<?> update(Object dto){
        return replyDBUpdate((ReplyUpdateRequestDTO) dto);
    }
    @Override
    public CompletableFuture<?> delete(Object dto){
        return  replyDBDelete((ReplyDeleteRequestDTO) dto);
    }

    @Async("postReplyExecutor")
    @Transactional
    public CompletableFuture<Long> replyDBInsert(ReplyInsertRequestDTO replyInsertRequestDTO) {
        try{

            ReplyEntity replyInsertEntity = PostModelMapperHelper.replyToEntity(replyInsertRequestDTO);

            replyMapper.insertReply(replyInsertEntity);

            long postId = replyInsertEntity.getPostId();

            return CompletableFuture.completedFuture(postId);

        } catch (Exception e) {
            log.error("DB insert fail", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("postReplyExecutor")
    @Transactional
    public CompletableFuture<Void> replyDBUpdate(ReplyUpdateRequestDTO replyUpdateRequestDTO) {
        try{

            ReplyEntity replyUpdateEntity = PostModelMapperHelper.replyToEntity(replyUpdateRequestDTO);

            replyMapper.updateReply(replyUpdateEntity);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {

            log.error("DB Update Fail", e);

            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("postReplyExecutor")
    @Transactional
    public CompletableFuture<Long> replyDBDelete(ReplyDeleteRequestDTO replyDeleteRequestDTO) {
        try{

            ReplyEntity replyDeleteEntity = PostModelMapperHelper.replyToEntity(replyDeleteRequestDTO);

            replyMapper.deleteReply(replyDeleteEntity);

            long postId = replyDeleteEntity.getPostId();


            return CompletableFuture.completedFuture(postId);

        } catch (Exception e) {

            log.error("DB Delete Fail", e);

            return CompletableFuture.failedFuture(e);
        }
    }
}
