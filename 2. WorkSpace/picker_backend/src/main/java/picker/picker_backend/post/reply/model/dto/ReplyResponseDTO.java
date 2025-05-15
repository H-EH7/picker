package picker.picker_backend.post.reply.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponseDTO {
    private String tempId;
    private Status status;
    private EventType eventType;
}
