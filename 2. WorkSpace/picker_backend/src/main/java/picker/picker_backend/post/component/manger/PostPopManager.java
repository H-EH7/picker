package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.config.PostPopProperties;

import java.time.Instant;

@Slf4j
@Component
@AllArgsConstructor

public class PostPopManager {

    private final PostPopProperties postPopProperties;

    public double calculatePopScore(
            int todayViews,
            int totalViews,
            int commentCount,
            int likesCounts,
            Instant updatedTime
    ){

        Instant currentTIme = Instant.now();

        double todayScore = postPopProperties.getWeights().getTodayviews() * Math.log10(Math.max(todayViews, 1));

        double totalScore = postPopProperties.getWeights().getTotalviews() * Math.log10(Math.max(totalViews, 1));

        double commentScore = postPopProperties.getWeights().getComments() * Math.log10(Math.max(commentCount, 1));

        double likesScore = postPopProperties.getWeights().getLikes() * Math.log10(Math.max(likesCounts, 1));

        double secondsSinceUpdate = (double)(currentTIme.getEpochSecond() - updatedTime.getEpochSecond());

        double decay = secondsSinceUpdate / postPopProperties.getDecaytimeunit();

        return todayScore + totalScore + commentScore + likesScore - decay;
    }
}
