package picker.picker_backend.post.likes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.likes.mapper.LikesMapper;
import picker.picker_backend.post.likes.model.dto.LikesSelectDTO;
import picker.picker_backend.post.likes.model.entity.LikesEntity;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikesQueryServiceImpl implements LikesQueryService{

    private final LikesMapper likesMapper;

    @Override
    public ResponseEntity<PostApiResponseWrapper<List<LikesSelectDTO>>> getLikesByUserId(String userId) {

        List<LikesEntity> userLikesList = likesMapper.getLikesByUserId(userId);

        List<LikesSelectDTO> likesSelectDTOList = userLikesList.stream().map(entity->
            new LikesSelectDTO(
                    entity.getUserId(),
                    entity.getPostId()
            )).toList();

        return ResponseEntity.ok(PostApiResponseWrapper.success(likesSelectDTOList, "User Likes Select Success"));
    }
}
