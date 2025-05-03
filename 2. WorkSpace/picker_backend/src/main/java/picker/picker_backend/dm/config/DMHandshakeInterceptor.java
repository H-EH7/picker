package picker.picker_backend.dm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import picker.picker_backend.dm.component.user.DMUserManager;
import picker.picker_backend.dm.exception.NoLoginUserException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DMHandshakeInterceptor implements HandshakeInterceptor {
    private final DMUserManager dmUserManager;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            String userId = dmUserManager.getUserId(request);
            attributes.put("userId", userId);

            return true;
        } catch (NoLoginUserException ex) {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
