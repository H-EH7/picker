package picker.picker_backend.post.factory;

import org.springframework.stereotype.Component;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

@Component
public class PostApiResponseFactory {

    public PostApiResponseWrapper<PostResponseDTO> buildResponse(String tempId, PostStatus postStatus, PostEventType postEventType){

        if(postStatus == PostStatus.SUCCESS){

            return PostApiResponseWrapper.success(
                    new PostResponseDTO(tempId, postStatus,postEventType),
                    postEventType.name() + " Success"
            );
        }

        if(postStatus == PostStatus.PROCESSING || postStatus == PostStatus.DLQ_PROCESSING){

            return PostApiResponseWrapper.fail(
                    new PostResponseDTO(tempId,postStatus,postEventType),
                    postEventType.name() + " Processing"
            );
        }

        if(postStatus == PostStatus.FAILED || postStatus == PostStatus.DLQ_FAILED){

            return PostApiResponseWrapper.fail(
                    new PostResponseDTO(tempId,postStatus,postEventType),
                    postEventType.name() + " Failed"
            );
        }

        return null;
    }
}
