package picker.picker_backend.post.factory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

@Component
public class PostApiResponseFactory {

    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> buildResponse(String tempId, PostStatus postStatus, PostEventType postEventType){

        PostResponseDTO postResponseDTO = new PostResponseDTO(tempId, postStatus, postEventType);
        String postResponseMessage = postEventType.name() + " " + postStatus.name();

        if(postStatus == PostStatus.SUCCESS){
            return ResponseEntity.ok(PostApiResponseWrapper.success(postResponseDTO,postResponseMessage));
        }

        if(postStatus == PostStatus.FAILED || postStatus == PostStatus.DLQ_FAILED){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(PostApiResponseWrapper.fail(postResponseDTO,postResponseMessage));
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(PostApiResponseWrapper.processing(postResponseDTO,postResponseMessage));

    }
}
