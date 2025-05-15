package picker.picker_backend.post.reply.mapper;

import org.apache.ibatis.annotations.Mapper;
import picker.picker_backend.post.model.entity.PostEntity;
import picker.picker_backend.post.reply.model.entity.ReplyEntity;

import java.util.List;

@Mapper
public interface ReplyMapper {
    List<ReplyEntity> getReply(Long postId);
    void insertReply(ReplyEntity replyEntity);
    void updateReply(ReplyEntity replyEntity);
    void deleteReply(ReplyEntity replyEntity);

}
