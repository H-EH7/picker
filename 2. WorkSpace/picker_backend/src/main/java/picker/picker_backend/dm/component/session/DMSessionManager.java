package picker.picker_backend.dm.component.session;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

public interface DMSessionManager {
    void putSession(WebSocketSession session);
    void removeSession(WebSocketSession session, CloseStatus status);
    boolean hasSession(String userId);
    WebSocketSession getSession(String userId);
}
