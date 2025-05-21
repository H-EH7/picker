package picker.picker_backend.post.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.model.entity.PostEntity;
import picker.picker_backend.post.schedular.mapper.BatchMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class PostViewCountBatchConfig {

    private final PostRedisViewCountManager postRedisViewCountManager;
    private final SqlSessionFactory sqlSessionFactory;
    private final JobRepository jobRepository;

    private static final int BATCH_SIZE = 1000;

    @Bean
    public Job postViewCountJob(Step postViewCountStep){
        return new JobBuilder("postViewCountJob", jobRepository).start(postViewCountStep).build();
    }

    @Bean
    public Step postViewCountStep(ItemReader<PostEntity> redisViewCountReader,
                                  ItemProcessor<PostEntity, PostEntity> processor,
                                  ItemWriter<PostEntity> mybatisBatchWriter,
                                  PlatformTransactionManager transactionManager){
        return new StepBuilder("postViewCountStep", jobRepository)
                .<PostEntity, PostEntity>chunk(BATCH_SIZE, transactionManager)
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
                BatchMapper batchMapper = session.getMapper(BatchMapper.class);
                for(PostEntity post : items){
                    batchMapper.updateViewCountBatch(post);
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
                postEntity.setViewCount((int)Double.parseDouble(e.getValue().toString()));
                return postEntity;
            }).toList();
    }

}
