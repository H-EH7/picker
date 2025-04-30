package picker.picker_backend.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import picker.picker_backend.post.model.entity.PostEntity;

@Mapper
public interface PostMapper {

    PostEntity findById(String userId);
}
