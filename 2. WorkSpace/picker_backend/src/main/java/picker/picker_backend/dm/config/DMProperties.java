package picker.picker_backend.dm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dm")
@Getter
@Setter
public class DMProperties {
    private String serverId;
    private Kafka kafka = new Kafka();

    @Getter
    @Setter
    public static class Kafka {
        private String topic;
        private String groupId;
    }
}
