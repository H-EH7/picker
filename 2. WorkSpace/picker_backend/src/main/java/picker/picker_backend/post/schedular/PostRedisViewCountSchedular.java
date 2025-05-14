package picker.picker_backend.post.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class PostRedisViewCountSchedular {

    private final PostRedisViewCountManager postRedisViewCountManager;
    private final SqlSessionFactory sqlSessionFactory;
    private static final int BATCH_SIZE =1000;

    @Scheduled(cron = "0 0 0 * * ?")
    private void postViewCountScheduler(){
        try{

            Map<Object,Object> redisViewCount = getPostViewCountRedis();

            if(redisViewCount.isEmpty()){
                return;
            }

            List<PostEntity> postEntityList = toPostEntity(redisViewCount);

            int[] updateDBResult = updatePostViewCount(postEntityList);

            if(updateDBResult.length > 0){
                initPostViewCountRedis();
            }else{
                log.error("batch error");
            }

        }catch (Exception e){

            log.error("batch error",e);
        }
    }

    public Map<Object, Object> getPostViewCountRedis(){

        return postRedisViewCountManager.getAllPostViewCount();
    }

    private int[] updatePostViewCount(List<PostEntity> redisViewCount){

        try(SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);) {
            PostMapper postMapper = session.getMapper(PostMapper.class);

            for(int i = 0; i < redisViewCount.size(); i++){
               postMapper.updateViewCountBatch(redisViewCount.get(i));

               if((i+1)% BATCH_SIZE == 0 || i+1 == redisViewCount.size()){
                   List<BatchResult> results = session.flushStatements();
                   return mergeUpdateCounts(results);
               }
           }
           session.commit();
        }catch (Exception e){

            log.error("batch error",e);

            throw e;
        }

            return new int[0];
    }

    private int[] mergeUpdateCounts(List<BatchResult> results){

        return results
                .stream()
                .flatMapToInt(result -> Arrays.stream(result.getUpdateCounts()))
                .toArray();
    }

    private List<PostEntity> toPostEntity(Map<Object, Object> redisList){

        return redisList.entrySet().stream()
                .map(e -> {
                    PostEntity redisViewCount = new PostEntity();
                    redisViewCount.setPostId(Long.parseLong(e.getKey().toString()));
                    redisViewCount.setViewCount(Integer.parseInt(e.getValue().toString()));
                    return redisViewCount;
                })
                .toList();
    }

    private void initPostViewCountRedis(){

            postRedisViewCountManager.initViewCount();
    }
}
