package picker.picker_backend.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.List;

@Mapper
public interface PostMapper {

    List<PostEntity> getPostById(String userId);
    void insertPost(PostEntity postEntity);
    void updatePost(PostEntity postEntity);
    void deletePost(PostEntity postEntity);
    List<PostEntity> getPostLists();
    PostEntity getPost(long postId);
    void updateViewCountBatch(PostEntity postEntity);
}
