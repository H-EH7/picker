package picker.picker_backend.post.likes.mapper;

import org.apache.ibatis.annotations.Mapper;
import picker.picker_backend.post.likes.model.entity.LikesEntity;

import java.util.List;

@Mapper
public interface LikesMapper {
    List<LikesEntity> getLikesByUserId(String userId);
    void insertLikes(LikesEntity likeEntity);
    void deleteLikes(LikesEntity likeEntity);

}
