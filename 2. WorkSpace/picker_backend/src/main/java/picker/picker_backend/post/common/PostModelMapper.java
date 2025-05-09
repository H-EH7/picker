package picker.picker_backend.post.common;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import picker.picker_backend.post.model.entity.PostEntity;

public class PostModelMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static PostEntity toEntity(Object postDTO){
        return modelMapper.map(postDTO, PostEntity.class);
    }


}
