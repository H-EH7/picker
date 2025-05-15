package picker.picker_backend.post.component.helper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.model.entity.PostEntity;
import picker.picker_backend.post.reply.model.entity.ReplyEntity;

@Component
public class PostModelMapperHelper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static PostEntity postToEntity(Object postDTO){
        return modelMapper.map(postDTO, PostEntity.class);
    }

    public static ReplyEntity replyToEntity(Object postDTO){
        return modelMapper.map(postDTO, ReplyEntity.class);
    }


}
