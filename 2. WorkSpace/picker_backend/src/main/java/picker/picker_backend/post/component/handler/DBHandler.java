package picker.picker_backend.post.component.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import picker.picker_backend.post.component.manger.DBManger;
import picker.picker_backend.post.postenum.EventType;

import java.util.Map;
@Slf4j
@AllArgsConstructor
public class DBHandler {

    @Getter
    private final DBManger dbManger;
    private final Map<EventType, Class<?>> dtoClassMap;

    public Class<?> getDtoClass(EventType eventType){
        return dtoClassMap.get(eventType);
    }
}
