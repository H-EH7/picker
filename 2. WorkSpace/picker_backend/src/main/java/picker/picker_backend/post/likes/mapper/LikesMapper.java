package picker.picker_backend.post.likes.mapper;

import org.apache.ibatis.annotations.Mapper;
import picker.picker_backend.post.likes.model.entity.LikesEntity;

import java.util.List;

@Mapper
public interface LikesMapper {
    List<LikesEntity> getLikes(Long postId);
    void insertReply(LikesEntity replyEntity);
    void deleteReply(LikesEntity replyEntity);

}
