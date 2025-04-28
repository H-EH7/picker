package picker.picker_backend.dm.model.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DM {
    private Long messageId;
    private Long roomId;
    private String senderId;
    private String receiverId;
    private String messageText;
    private Timestamp sendAt;
    private Timestamp readAt;
    private Long parentMessageId;
}
