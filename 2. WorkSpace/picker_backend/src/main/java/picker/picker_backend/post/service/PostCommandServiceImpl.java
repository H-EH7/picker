package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.kafka.producer.PostEventProducer;
import picker.picker_backend.post.mapper.PostMapper;
import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;
import picker.picker_backend.post.model.entity.PostEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {


    private final PostEventProducer postEventProducer;


    @Override
    public int insertPost(PostInsertDTO postInsertDTO){

        try{

            return 0;
        } catch (RuntimeException e) {

            log.error("Error",e);

            throw e;

        } catch (Exception e) {

            log.error("Error",e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public int updatePost(PostUpdateDTO postUpdateDTO){
        try{


            return 0;

        } catch (RuntimeException e) {

            log.error("Error",e);

            throw e;

        } catch (Exception e) {

            log.error("Error",e);

            throw new RuntimeException(e);
        }
    }

}
