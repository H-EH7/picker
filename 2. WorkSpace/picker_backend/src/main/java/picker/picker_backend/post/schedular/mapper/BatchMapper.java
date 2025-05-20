package picker.picker_backend.post.schedular.mapper;

import org.apache.ibatis.annotations.Mapper;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.List;

@Mapper
public interface BatchMapper {
    void updateViewCountBatch(PostEntity postEntity);
    List<PostEntity> getViewLikesCounts();
    void insertPostAggregate(PostEntity postEntity);
    void deletePopPost();

}
