package picker.picker_backend.dm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import picker.picker_backend.dm.component.DMWebSocketHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class DMWebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler dmWebSocketHandler;
    private final HandshakeInterceptor dmHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(dmWebSocketHandler, "/ws")
                .addInterceptors(dmHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
