package picker.picker_backend.post.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "post")
public class PostProperties {

    private Kafka kafka;

    @Getter
    @Setter
    public static class Kafka{
        private String topic;
        private String groupId;
        private int maxRetries;
    }

}
