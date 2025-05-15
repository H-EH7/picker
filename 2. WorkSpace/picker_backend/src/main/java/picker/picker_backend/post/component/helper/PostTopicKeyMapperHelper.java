package picker.picker_backend.post.component.helper;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.config.PostProperties;
import picker.picker_backend.post.postenum.TopicKey;

import java.util.HashMap;
import java.util.Map;

@Component
public class PostTopicKeyMapperHelper {

    private final PostProperties postProperties;
    private Map<TopicKey, String> topicKeyToNameMap;
    private Map<String, TopicKey> topicNameToKeyMap;


    public PostTopicKeyMapperHelper(PostProperties postProperties){
        this.postProperties = postProperties;
    }

    @PostConstruct
    public void init(){
        topicKeyToNameMap = new HashMap<>();
        topicNameToKeyMap = new HashMap<>();

        postProperties.getKafka().getTopic().forEach((key, value) -> {
            TopicKey topicKey = TopicKey.valueOf(key.toUpperCase());
            topicKeyToNameMap.put(topicKey, value);
            topicNameToKeyMap.put(value, topicKey);
        });
    }

    public String getTopicName(TopicKey key){
        return topicKeyToNameMap.get(key);
    }

    public TopicKey getTopicKey(String topicName){
        return  topicNameToKeyMap.get(topicName);
    }
}
