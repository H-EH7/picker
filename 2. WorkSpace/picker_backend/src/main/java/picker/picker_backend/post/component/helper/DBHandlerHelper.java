package picker.picker_backend.post.component.helper;

import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.handler.DBHandler;
import picker.picker_backend.post.component.manger.PostDBManager;
import picker.picker_backend.post.model.dto.PostDeleteRequestDTO;
import picker.picker_backend.post.model.dto.PostInsertRequestDTO;
import picker.picker_backend.post.model.dto.PostUpdateRequestDTO;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class DBHandlerHelper {
    private final Map<String, DBHandler> DBMap = new HashMap<>();

    public DBHandlerHelper(PostDBManager postDBManager){
        DBMap.put(TopicKey.POST.name().toLowerCase(), new DBHandler(postDBManager, Map.of(
                EventType.INSERT, PostInsertRequestDTO.class,
                EventType.UPDATE, PostUpdateRequestDTO.class,
                EventType.DELETE, PostDeleteRequestDTO.class
        )));
    }

    public DBHandler getDBhandler(String topic){
        return DBMap.get(topic.toLowerCase());
    }
}
