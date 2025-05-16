package picker.picker_backend.post.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostRedisLikesSchedular {

    @Scheduled(cron = "0 0 0 * * ?")
    private void postLikesSchedular(){

    }

}
