package picker.picker_backend.dm.component.session;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

public abstract class AbstractDMSessionManager implements DMSessionManager {
    String extractUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }
}
