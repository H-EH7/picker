package picker.picker_backend.post.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.*;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostPopCalculateSchedular {

    private final LikesRedisManger likesRedisManger;
    private final PostRedisViewCountManager postRedisViewCountManager;
    private final PostRedisReplyCountManager postRedisReplyCountManager;
    private final PostPopManager postPopManager;

    @Scheduled(cron = "0 15 * * * ?")
    public void popCalculate(){

        double popScore = postPopManager.calculatePopScore(1,1,1, 1,Instant.now());

    }
}
