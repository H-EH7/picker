package picker.picker_backend.post.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.entity.PostEntity;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class PostViewCountBatchConfig {

    private final PostRedisViewCountManager postRedisViewCountManager;
    private final SqlSessionFactory sqlSessionFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int BATCH_SIZE = 1000;

    @Bean
    public Job postViewCountJob(Step postViewCountStep){
        return jobBuilderFactory.get("postViewCountJob").start(postViewCountStep).build();
    }

    @Bean
    public Step postViewCountStep(ItemReader<PostEntity> redisViewCountReader,
                                  ItemProcessor<PostEntity, PostEntity> processor,
                                  ItemWriter<PostEntity> mybatisBatchWriter){
        return stepBuilderFactory.get("postViewCountStep")
                .<PostEntity, PostEntity>chunk(BATCH_SIZE)
                .reader(redisViewCountReader)
                .processor(processor)
                .writer(mybatisBatchWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<PostEntity> redisViewCountReader(){
        List<PostEntity> postEntityList = toPostEntity(postRedisViewCountManager.getAllPostViewCount());
        return new ListItemReader<>(postEntityList);
    }

    @Bean
    @StepScope
    public ItemProcessor<PostEntity, PostEntity> processor(){
        return item -> item;
    }

    @Bean
    @StepScope
    public ItemWriter<PostEntity> mybatisBatchWriter(){
        return items ->{
            try(SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)){
                PostMapper postMapper = session.getMapper(PostMapper.class);
                for(PostEntity post : items){
                    postMapper.updateViewCountBatch(post);
                }
                session.commit();
            }catch (Exception e){
                log.error("batch update error ", e);
                throw e;
            }
        };
    }

    private static List<PostEntity> toPostEntity(Map<Object, Object> redisList){
            return redisList.entrySet().stream().map(e ->{
                PostEntity postEntity = new PostEntity();
                postEntity.setPostId(Long.parseLong(e.getKey().toString()));
                postEntity.setViewCount(Integer.parseInt(e.getValue().toString()));
                return postEntity;
            }).toList();
    }

}
