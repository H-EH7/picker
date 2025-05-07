package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.dto.PostSelectDTO;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostQuerySeviceImpl implements  PostQueryService{

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<PostSelectDTO> getPostById(String userId){
        try{
            List<PostEntity> postEntities = postMapper.getPostById(userId);

            if(postEntities == null){
                return null;
            }

            return postEntities.stream().map(postEntity -> new PostSelectDTO(
                    postEntity.getUserId(),
                    postEntity.getPostId(),
                    postEntity.getPostText(),
                    postEntity.getCreatedAt(),
                    postEntity.getUpdatedAt())).collect(Collectors.toList());

        } catch (RuntimeException e) {

            log.error("Error",e);

            throw e;

        } catch (Exception e) {

            log.error("Error",e);

            throw new RuntimeException(e);
        }
    }
}
