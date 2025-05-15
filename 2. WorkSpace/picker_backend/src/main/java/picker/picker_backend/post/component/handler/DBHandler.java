package picker.picker_backend.post.component.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picker.picker_backend.post.component.manger.DBManger;
import picker.picker_backend.post.postenum.EventType;

import java.util.Map;
@Slf4j
@AllArgsConstructor
public class DBHandler {


    private final DBManger dbManger;
    private final Map<EventType, Class<?>> dtoClassMap;

    public DBManger getDbManger(){
        return dbManger;
    }

    public Class<?> getDtoClass(EventType eventType){
        return dtoClassMap.get(eventType);
    }
}
