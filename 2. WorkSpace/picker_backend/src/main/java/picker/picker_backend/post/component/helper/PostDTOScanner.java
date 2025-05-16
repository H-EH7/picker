package picker.picker_backend.post.component.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.annotation.PostAnnotation;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Slf4j
@Component
public class PostDTOScanner {

    public Map<TopicKey, Map<EventType, Class<?>>> scanDtoMappings() {
        try {
            Map<TopicKey, Map<EventType, Class<?>>> dtoMappings = new HashMap<>();

            Reflections reflections = new Reflections("picker.picker_backend.post");

            Set<Class<?>> dtoClasses = reflections.getTypesAnnotatedWith(PostAnnotation.class);

            for (Class<?> dtoclass : dtoClasses) {
                PostAnnotation annotation = dtoclass.getAnnotation(PostAnnotation.class);
                if(annotation != null){
                    TopicKey topicKey = annotation.topicKey();
                    EventType eventType = annotation.eventType();
                    dtoMappings.computeIfAbsent(topicKey, k -> new HashMap<>()).put(eventType, dtoclass);
                }
            }

            return dtoMappings;

        } catch (Exception e) {

            log.error("DTO Scanner error ", e);

            throw e;
        }
    }
}
