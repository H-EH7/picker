package picker.picker_backend.post.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.entity.PostEntity;

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

            boolean updateDBResult = updatePostViewCount(postEntityList);

            if(updateDBResult){
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

    private boolean updatePostViewCount(List<PostEntity> redisViewCount){
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            PostMapper postMapper = session.getMapper(PostMapper.class);

            for(int i = 0; i < redisViewCount.size(); i++){
               postMapper.updateViewCountBatch(redisViewCount.get(i));

               if((i+1)% BATCH_SIZE == 0 || i+1 == redisViewCount.size()){
                   session.flushStatements();
               }
           }

           session.commit();

            return true;

        }catch (Exception e){
            session.rollback();
            log.error("batch error",e);
            return false;
        }finally {
            session.close();
        }
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
