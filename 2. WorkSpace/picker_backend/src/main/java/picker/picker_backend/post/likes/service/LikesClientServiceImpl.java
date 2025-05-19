package picker.picker_backend.post.likes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.manger.LikesRedisManger;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.kafka.producer.PostEventProducer;
import picker.picker_backend.post.likes.model.dto.LikesDeleteRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesInsertRequestDTO;
import picker.picker_backend.post.postenum.EventType;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikesClientServiceImpl implements LikesClientService {

    private final LikesRedisManger likesRedisManger;
    private final PostEventProducer postEventProducer;

    @Override
    public ResponseEntity<PostApiResponseWrapper<LikesInsertRequestDTO>> insertLikes(LikesInsertRequestDTO likesInsertRequestDTO) {

        likesRedisManger.addLikes(likesInsertRequestDTO.getPostId(), likesInsertRequestDTO.getUserId());

        postEventProducer.sendLikeEvent(likesInsertRequestDTO, EventType.INSERT);

        return ResponseEntity.ok(PostApiResponseWrapper.success(likesInsertRequestDTO, "Likes Success"));
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<LikesDeleteRequestDTO>> deleteLikes(LikesDeleteRequestDTO likesDeleteRequestDTO) {

        likesRedisManger.removeLikes(likesDeleteRequestDTO.getPostId(),likesDeleteRequestDTO.getUserId());

        postEventProducer.sendLikeEvent(likesDeleteRequestDTO, EventType.DELETE);

        return ResponseEntity.ok(PostApiResponseWrapper.success(likesDeleteRequestDTO, "DisLikes Success"));
    }

    @Override
    public long getLikesCount(long postId) {

        return likesRedisManger.getLikeCount(postId);
    }
}
