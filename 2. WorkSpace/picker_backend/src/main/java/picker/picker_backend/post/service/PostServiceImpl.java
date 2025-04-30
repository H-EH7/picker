package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.dto.PostDTO;
import picker.picker_backend.post.model.entity.PostEntity;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    @Autowired
    private PostMapper postMapper;

    @Override
    public PostDTO getPostById(String userId){
        PostEntity postEntity = postMapper.findById(userId);
        System.out.println(postEntity);
        if(postEntity == null){
            return null;
        }

      return new PostDTO(
              postEntity.getUserId()
      );
    };

}
