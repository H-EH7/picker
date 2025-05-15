package picker.picker_backend.post.factory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;

@Component
public class PostApiResponseFactory {

    public static ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> buildResponse(String tempId, Status status, EventType eventType, TopicKey topicKey){

        PostResponseDTO postResponseDTO = new PostResponseDTO(tempId, status, eventType, topicKey);
        String postResponseMessage = eventType.name() + " " + status.name();

        if(status == Status.SUCCESS){
            return ResponseEntity.ok(PostApiResponseWrapper.success(postResponseDTO,postResponseMessage));
        }

        if(status == Status.FAILED || status == Status.DLQ_FAILED){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(PostApiResponseWrapper.fail(postResponseDTO,postResponseMessage));
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(PostApiResponseWrapper.processing(postResponseDTO,postResponseMessage));

    }
}
