package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostSelectDTO;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements  PostQueryService{

    @Autowired
    private PostMapper postMapper;
    private final PostRedisViewCountManager postRedisViewCountManager;

    @Override
    public PostSelectDTO getPost(long postId) {
        try{
            PostEntity postEntity = postMapper.getPost(postId);

            if(postEntity == null){
                return null;
            }

            PostSelectDTO postSelectDTO = new PostSelectDTO(
                    postEntity.getUserId(),
                    postEntity.getPostId(),
                    postEntity.getPostText(),
                    postEntity.getCreatedAt(),
                    postEntity.getUpdatedAt(),
                    postEntity.getTempId(),
                    postEntity.getViewCount()
            );

            postRedisViewCountManager.incrementViewCount(postEntity.getPostId());

            return postSelectDTO;

        } catch (Exception e) {

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<List<PostSelectDTO>>> getPostById(String userId){
        try{
            List<PostEntity> postEntities = postMapper.getPostById(userId);

            if(postEntities == null || postEntities.isEmpty()){
                return ResponseEntity.status(404).body(
                        PostApiResponseWrapper.fail(null, "Select Fail")
                );
            }

            List<PostSelectDTO> postSelectDTOList = postEntities.stream().map(
                    postEntity -> new PostSelectDTO(
                            postEntity.getUserId(),
                            postEntity.getPostId(),
                            postEntity.getPostText(),
                            postEntity.getCreatedAt(),
                            postEntity.getUpdatedAt(),
                            postEntity.getTempId(),
                            postEntity.getViewCount()
                    )).collect(Collectors.toList());

            return ResponseEntity.ok(
                    PostApiResponseWrapper.success(postSelectDTOList, "Select Success")
            );

        } catch (Exception e) {

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<List<PostSelectDTO>>> getPostLists() {
        try {

            List<PostEntity> postEntities = postMapper.getPostLists();

            if(postEntities == null || postEntities.isEmpty()){
                return ResponseEntity.status(404).body(
                        PostApiResponseWrapper.fail(null, "Select Fail")
                );
            }

            List<PostSelectDTO> postSelectDTOList = postEntities.stream().map(
                    postEntity -> new PostSelectDTO(
                            postEntity.getUserId(),
                            postEntity.getPostId(),
                            postEntity.getPostText(),
                            postEntity.getCreatedAt(),
                            postEntity.getUpdatedAt(),
                            postEntity.getTempId(),
                            postEntity.getViewCount()
                    )).collect(Collectors.toList());

            return ResponseEntity.ok(
                    PostApiResponseWrapper.success(postSelectDTOList, "Select Success")
            );

        } catch (Exception e) {

            log.error("Error", e);
            throw e;
        }
    }
}
