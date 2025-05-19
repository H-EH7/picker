package picker.picker_backend.post.component.manger;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import picker.picker_backend.post.component.helper.PostModelMapperHelper;
import picker.picker_backend.post.likes.mapper.LikesMapper;
import picker.picker_backend.post.likes.model.dto.LikesDeleteRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesInsertRequestDTO;
import picker.picker_backend.post.likes.model.entity.LikesEntity;
import picker.picker_backend.post.postenum.TopicKey;

import java.util.concurrent.CompletableFuture;
@Slf4j
@Component
@RequiredArgsConstructor
public class LikesDBManager implements DBManger, TopicKeyProvider{

    private final LikesMapper likesMapper;

    @Override
    public TopicKey getTopicKey() {
        return TopicKey.LIKES;
    }

    @Override
    public CompletableFuture<?> insert(Object dto) {
        return likesDBInsert((LikesInsertRequestDTO) dto);
    }

    @Override
    public CompletableFuture<?> update(Object dto) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<?> delete(Object dto) {
        return likesDBDelete((LikesDeleteRequestDTO) dto);
    }

    @Async("likesExecutor")
    @Transactional
    public CompletableFuture<Long> likesDBInsert(LikesInsertRequestDTO likesInsertRequestDTO) {
        try{

            LikesEntity likesInsertEntity = PostModelMapperHelper.likesToEntity(likesInsertRequestDTO);

            likesMapper.insertLikes(likesInsertEntity);

            long postId = likesInsertEntity.getPostId();

            return CompletableFuture.completedFuture(postId);

        } catch (Exception e) {
            log.error("DB insert fail", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("likesExecutor")
    @Transactional
    public CompletableFuture<Long> likesDBDelete(LikesDeleteRequestDTO likesDeleteRequestDTO) {
        try{

            LikesEntity likesDeleteEntity = PostModelMapperHelper.likesToEntity(likesDeleteRequestDTO);

            likesMapper.deleteLikes(likesDeleteEntity);

            long postId = likesDeleteEntity.getPostId();

            return CompletableFuture.completedFuture(postId);

        } catch (Exception e) {

            log.error("DB Delete Fail", e);

            return CompletableFuture.failedFuture(e);
        }
    }
}
