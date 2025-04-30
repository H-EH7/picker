package picker.picker_backend.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.List;

@Mapper
public interface PostMapper {

    List<PostEntity> getPostById(String userId);
    int insertPost(PostEntity postEntity);
    int updatePost(PostEntity postEntity);

}
