package picker.picker_backend.dm.component.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MemoryDMSessionManager extends AbstractDMSessionManager {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /// WebSocket 세션 저장
    @Override
    public void putSession(WebSocketSession session) {
        String userId = extractUserId(session);
        sessions.put(userId, session);
    }

    /// WebSocket 세션 삭제
    @Override
    public void removeSession(WebSocketSession session, CloseStatus status) {
        String userId = extractUserId(session);
        WebSocketSession removed = sessions.remove(userId);
        if(removed != null && removed.isOpen()) {
            try {
                removed.close(status);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /// Session 존재 여부 조회
    @Override
    public boolean hasSession(String userId) {
        return sessions.containsKey(userId);
    }

    /// Session 조회
    @Override
    public WebSocketSession getSession(String userId) {
        return sessions.get(userId);
    }
}
