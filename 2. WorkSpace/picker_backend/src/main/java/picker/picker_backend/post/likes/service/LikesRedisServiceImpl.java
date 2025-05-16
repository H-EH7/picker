package picker.picker_backend.post.likes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.manger.LikesRedisManger;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.likes.model.dto.LikesDeleteRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesInsertRequestDTO;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikesRedisServiceImpl implements LikesRedisService {

    private final LikesRedisManger likesRedisManger;

    @Override
    public ResponseEntity<PostApiResponseWrapper<LikesInsertRequestDTO>> insertLikes(LikesInsertRequestDTO likesInsertRequestDTO) {

        likesRedisManger.addLikes(likesInsertRequestDTO.getPostId(), likesInsertRequestDTO.getUserId());

        return ResponseEntity.ok(PostApiResponseWrapper.success(likesInsertRequestDTO, "Likes Success"));
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<LikesDeleteRequestDTO>> deleteLikes(LikesDeleteRequestDTO likesDeleteRequestDTO) {

        likesRedisManger.removeLikes(likesDeleteRequestDTO.getPostId(),likesDeleteRequestDTO.getUserId());

        return ResponseEntity.ok(PostApiResponseWrapper.success(likesDeleteRequestDTO, "DisLikes Success"));
    }

    @Override
    public long getLikesCount(long postId) {

        return likesRedisManger.getLikeCount(postId);
    }
}
