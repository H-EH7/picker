package picker.picker_backend.post.component.helper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.handler.DBHandler;
import picker.picker_backend.post.component.manger.DBManger;
import picker.picker_backend.post.component.manger.TopicKeyProvider;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DBHandlerHelper {
    private final Map<String, DBHandler> DBMap = new HashMap<>();
    private final PostDTOScanner postDTOScanner;
    private final List<DBManger> dbMangers;

   @PostConstruct
    public void init(){
       Map<TopicKey, Map<EventType, Class<?>>> dtoMap = postDTOScanner.scanDtoMappings();

       for(DBManger dbManger : dbMangers){
           if(dbManger instanceof TopicKeyProvider provider){
               TopicKey topicKey = provider.getTopicKey();
               Map<EventType, Class<?>> dtoMapping = dtoMap.get(topicKey);
               if(dtoMapping != null){
                   DBMap.put(topicKey.name().toLowerCase(), new DBHandler(dbManger, dtoMapping));
               }
           }
       }
    }


    public DBHandler getDBhandler(String topic){
        return DBMap.get(topic.toLowerCase());
    }
}
