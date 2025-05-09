package picker.picker_backend.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import picker.picker_backend.post.common.PostModelMapper;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostDBService{

    private final PostMapper postMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Async("postExecutor")
    @Transactional
    public CompletableFuture<Void> postDBInsert(PostInsertDTO postInsertDTO) {
        try{
            PostEntity postInsertEntity = PostModelMapper.toEntity(postInsertDTO);

            postMapper.insertPost(postInsertEntity);

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("DB insert fail", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async("postExecutor")
    @Transactional
    public CompletableFuture<Void> postDBUpdate(PostUpdateDTO postUpdateDTO) {
        try{
            PostEntity postUpdateEntity = PostModelMapper.toEntity(postUpdateDTO);

            postMapper.updatePost(postUpdateEntity);

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {

                log.error("DB Update Fail", e);

            return CompletableFuture.failedFuture(e);
        }
    }
}
