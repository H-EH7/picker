package picker.picker_backend.post.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;


@Slf4j
@Component
@RequiredArgsConstructor
public class PostSchedular {

    private final JobLauncher jobLauncher;
    private final Job postViewCountJob;
    private final PostRedisViewCountManager postRedisViewCountManager;

    @Scheduled(cron = "0 0 0 * * ?")
    private void postBatchScheduler() throws Exception{

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

        JobExecution executionViewCount = jobLauncher.run(postViewCountJob,jobParameters);

        if(executionViewCount.getStatus() == BatchStatus.COMPLETED){
            postRedisViewCountManager.initViewCount();
        }
    }


    @Scheduled(cron = "0 15 * * * ?")
    private void popPostScoreSchedular(){



    }
}
