package picker.picker_backend.post.schedular.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.model.dto.PostPopDTO;
import picker.picker_backend.post.model.entity.PostEntity;
import picker.picker_backend.post.schedular.mapper.BatchMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchedularService {

    private final BatchMapper batchMapper;

    public Map<Long,PostPopDTO> getPopPostByDB(){

        List<PostEntity> popPostList = batchMapper.getPopPost();

        Map<Long, PostPopDTO> popPostDTOList =  new HashMap<>();
        for(PostEntity entity : popPostList){
            popPostDTOList.put(
                    entity.getPostId(),
                    PostPopDTO.builder()
                            .postId(entity.getPostId())
                            .replyCount(0)
                            .likesCount(0)
                            .todayViewCount(entity.getViewCount())
                            .updatedAt(entity.getUpdatedAt())
                            .todayViewCount(0)
                            .build());
        }

        return popPostDTOList;
    }

    public void getPopPostByRedis(){

    }

}
