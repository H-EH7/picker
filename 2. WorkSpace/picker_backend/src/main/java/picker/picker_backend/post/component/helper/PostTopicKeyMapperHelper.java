package picker.picker_backend.post.component.helper;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.config.PostProperties;
import picker.picker_backend.post.postenum.TopicKey;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public TopicKey getDLQTopicKey(String topicName){

        if(topicName == null) return null;

        Pattern dlqPattern = Pattern.compile("^(.*)(DLQ)$",Pattern.CASE_INSENSITIVE);
        Matcher matcher = dlqPattern.matcher(topicName);

        if(matcher.matches()){
            String baseTopic = matcher.group(1);
            try{
                return TopicKey.valueOf(baseTopic.toUpperCase());
            }catch (Exception e){
                return null;
            }
        }
        return null;
    }
}
