package picker.picker_backend.dm.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class DMSessionManager {
    private final StringRedisTemplate redisTemplate;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /// WebSocket 세션 저장
    public void putSession(WebSocketSession session) {
        String userId = extractUserId(session);
        sessions.put(userId, session);
    }

    /// WebSocket 세션 삭제
    public void removeSession(WebSocketSession session) {
        String userId = extractUserId(session);
        sessions.remove(userId);
    }

    /// Session 존재 여부 조회
    public boolean hasSession(String userId) {
        return sessions.containsKey(userId);
    }

    /// Session 조회
    public WebSocketSession getSession(String userId) {
        return sessions.get(userId);
    }

    private String extractUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }
}
