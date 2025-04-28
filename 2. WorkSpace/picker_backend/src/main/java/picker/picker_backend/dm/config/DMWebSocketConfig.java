package picker.picker_backend.dm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class DMWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 최초 연결할 엔드포인트
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS는 WebSocket이 안 되는 환경에서도 fallback 가능
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 경로 prefix
        registry.enableSimpleBroker("/dm"); // 메시지 구독 경로 (브로커)
        // 송신 경로 prefix
        registry.setApplicationDestinationPrefixes("/app"); // 메시지 보낼 때 클라이언트가 사용하는 경로
    }
}
