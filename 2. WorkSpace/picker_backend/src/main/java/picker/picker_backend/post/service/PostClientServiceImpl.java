package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.kafka.producer.PostEventProducer;
import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostClientServiceImpl implements PostClientService {


    private final PostEventProducer postEventProducer;


    @Override
    public boolean insertPost(PostInsertDTO postInsertDTO){

        try{

            postEventProducer.sendPostEvent(postInsertDTO, "insert");

            return true;

        } catch (Exception e) {

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public boolean updatePost(PostUpdateDTO postUpdateDTO){
        try{

            postEventProducer.sendPostEvent(postUpdateDTO, "update");

            return true;

        } catch (Exception e) {

            log.error("Error",e);

            throw e;

        }
    }

}
