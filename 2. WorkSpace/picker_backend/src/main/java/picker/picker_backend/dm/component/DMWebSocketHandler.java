package picker.picker_backend.dm.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import picker.picker_backend.dm.component.session.DMSessionManager;

@Component
@RequiredArgsConstructor
public class DMWebSocketHandler extends TextWebSocketHandler {
    private final DMSessionManager sessionManager;

    // 연결 성공 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionManager.putSession(session);
    }

    // 연결 종료 시
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.removeSession(session, status);
    }
}
