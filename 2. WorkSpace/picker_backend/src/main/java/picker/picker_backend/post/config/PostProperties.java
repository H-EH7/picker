package picker.picker_backend.post.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "post")
public class PostProperties {

    private Kafka kafka;

    @Getter
    @Setter
    public static class Kafka{
        private Map<String, String> topic;
        private String groupId;
    }

}
