package picker.picker_backend.post.component.manger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import picker.picker_backend.post.component.helper.PostModelMapperHelper;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.dto.PostDeleteRequestDTO;
import picker.picker_backend.post.model.dto.PostInsertRequestDTO;
import picker.picker_backend.post.model.dto.PostUpdateRequestDTO;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDBManager implements DBManger{

    private final PostMapper postMapper;

    @Override
    public CompletableFuture<?> insert(Object dto) {
        return postDBInsert((PostInsertRequestDTO) dto);
    }
    @Override
    public CompletableFuture<?> update(Object dto){
        return postDBUpdate((PostUpdateRequestDTO) dto);
    }
    @Override
    public CompletableFuture<?> delete(Object dto){
        return  postDBDelete((PostDeleteRequestDTO) dto);
    }

    @Async("postExecutor")
    @Transactional
    public CompletableFuture<Long> postDBInsert(PostInsertRequestDTO postInsertRequestDTO) {
        try{

            PostEntity postInsertEntity = PostModelMapperHelper.postToEntity(postInsertRequestDTO);

            postMapper.insertPost(postInsertEntity);

            long postId = postInsertEntity.getPostId();

            return CompletableFuture.completedFuture(postId);

        } catch (Exception e) {
            log.error("DB insert fail", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("postExecutor")
    @Transactional
    public CompletableFuture<Void> postDBUpdate(PostUpdateRequestDTO postUpdateRequestDTO) {
        try{

            PostEntity postUpdateEntity = PostModelMapperHelper.postToEntity(postUpdateRequestDTO);

            postMapper.updatePost(postUpdateEntity);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {

                log.error("DB Update Fail", e);

            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("postExecutor")
    @Transactional
    public CompletableFuture<Void> postDBDelete(PostDeleteRequestDTO postDeleteRequestDTO) {
        try{

            PostEntity postDeleteEntity = PostModelMapperHelper.postToEntity(postDeleteRequestDTO);

            postMapper.deletePost(postDeleteEntity);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {

            log.error("DB Delete Fail", e);

            return CompletableFuture.failedFuture(e);
        }
    }
}
