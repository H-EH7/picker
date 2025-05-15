package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@AllArgsConstructor
public class PostPopManager {

    private static final double TODAY_VIEWS_WEIGHT = 1.0;
    private static final double TOTAL_VIEWS_WEIGHT = 0.5;
    private static final double COMMENT_WEIGHT = 0.7;

    private static final double DECAY_TIME_UNIT = 45000.0;

    public static double calculatePopScore(
            int todayViews,
            int totalViews,
            int commentCount,
            Instant updatedTime
    ){
        Instant currentTIme = Instant.now();

        double todayScore = TODAY_VIEWS_WEIGHT * Math.log10(Math.max(todayViews, 1));
        double totalScore = TOTAL_VIEWS_WEIGHT * Math.log10(Math.max(totalViews, 1));
        double commentScore = COMMENT_WEIGHT * Math.log10(Math.max(commentCount, 1));

        double secondsSinceUpdate = (double)(currentTIme.getEpochSecond() - updatedTime.getEpochSecond());
        double decay = secondsSinceUpdate / DECAY_TIME_UNIT;

        return todayScore + totalScore + commentScore - decay;
    }
}
