package picker.picker_backend.post.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "post.popularity")
public class PostPopProperties {

    private weights weights;
    private double decaytimeunit;

    @Getter
    @Setter
    public static class weights{
        private double todayviews;
        private double totalviews;
        private double comments;
        private double likes;
    }
}
