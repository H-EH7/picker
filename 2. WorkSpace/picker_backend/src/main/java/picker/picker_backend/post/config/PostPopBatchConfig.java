package picker.picker_backend.post.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
import picker.picker_backend.post.component.manger.PostRedisReplyCountManager;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.model.entity.PostEntity;
import picker.picker_backend.post.schedular.mapper.BatchMapper;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PostPopBatchConfig {

    private final PostRedisViewCountManager postRedisViewCountManager;
    private final SqlSessionFactory sqlSessionFactory;
    private final JobRepository jobRepository;
    private final PostRedisReplyCountManager postRedisReplyCountManager;
    private final BatchMapper batchMapper;
    private static final int BATCH_SIZE = 1000;

    @Bean
    public Job postPopJob(Step postPopStep){
        return new JobBuilder("postPopJob", jobRepository).start(postPopStep).build();
    }

    @Bean
    public Step postPopStep(ItemReader<PostEntity> popReader,
                                  ItemProcessor<PostEntity, PostEntity> processorPop,
                                  ItemWriter<PostEntity> mybatisPopBatchWriter,
                                  PlatformTransactionManager transactionManager){
        return new StepBuilder("postPopStep", jobRepository)
                .<PostEntity, PostEntity>chunk(BATCH_SIZE, transactionManager)
                .reader(popReader)
                .processor(processorPop)
                .writer(mybatisPopBatchWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<PostEntity> popReader() {

        Map<Object, Object> topReplyCount = postRedisReplyCountManager.getTopPostReplyCount();

        List<PostEntity> resultPostEntityList;

        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {

            BatchMapper batchMapper = session.getMapper(BatchMapper.class);
            List<PostEntity> postEntityList = batchMapper.getViewLikesCounts();

            session.commit();

            resultPostEntityList = mergePostEntity(postEntityList, toPostEntity(topReplyCount));

        } catch (Exception e) {

            log.error("batch update error ", e);
            throw e;

        }

        return new ListItemReader<>(resultPostEntityList);
    }

    @Bean
    @StepScope
    public ItemProcessor<PostEntity, PostEntity> processorPop(){
        return item -> item;
    }

    @Bean
    @StepScope
    public ItemWriter<PostEntity> mybatisPopBatchWriter(){
        return items ->{
            try(SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)){
                BatchMapper batchMapper = session.getMapper(BatchMapper.class);
                batchMapper.deletePopPost();
                for(PostEntity post : items){
                    batchMapper.insertPostAggregate(post);
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


    private List<PostEntity> mergePostEntity(List<PostEntity> postEntityListA, List<PostEntity> postEntityListB){
        return Stream.concat(postEntityListA.stream(),postEntityListB.stream())
                .collect(Collectors.groupingBy(PostEntity::getPostId))
                .values()
                .stream()
                .map(this::mergeGroup)
                .toList();
    }

    private PostEntity mergeGroup(List<PostEntity> group){
        PostEntity result = new PostEntity();

        for(PostEntity entity : group) {
            mergeField(result::getPostId, entity.getPostId(), result::setPostId);
            mergeField(result::getLikeCount, entity.getLikeCount(), result::setLikeCount);
            mergeField(result::getViewCount, entity.getViewCount(), result::setViewCount);
            mergeField(result::getReplyCount, entity.getReplyCount(), result::setReplyCount);
        }

        return result;
    }

    private <T> void mergeField(Supplier<T> currentGetter, T newValue, Consumer<T> setter){
        if(currentGetter.get() == null && newValue != null){
            setter.accept(newValue);
        }
    }


}
