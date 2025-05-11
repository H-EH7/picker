package picker.picker_backend.Auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import picker.picker_backend.Auth.config.JWTconfig;
import picker.picker_backend.Auth.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JWTconfig JWTconfig;

    @Autowired
    private AuthService authService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username) {
        String accessToken = JWTconfig.generateAccessToken(username);
        String refreshToken = JWTconfig.generateRefreshToken(username);

        // Redis에 토큰 저장
        authService.saveAccessToken(username, accessToken);
        authService.saveRefreshToken(username, refreshToken);

        // Kafka로 이벤트 전송
        kafkaTemplate.send("auth-events", "User logged in: " + username);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        return ResponseEntity.ok(response);
    }

    // 토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refreshToken) {//HttpServlet으로?
        if (!JWTconfig.validateToken(refreshToken) || !"refresh".equals(JWTconfig.getTokenType(refreshToken))) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));
        }

        String username = JWTconfig.getUsernameFromToken(refreshToken);
        String storedRefreshToken = authService.getToken("refresh:" + username);

        if (!refreshToken.equals(storedRefreshToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token mismatch"));
        }

        String newAccessToken = JWTconfig.generateAccessToken(username);
        authService.saveAccessToken(username, newAccessToken);

        kafkaTemplate.send("auth-events", "Token refreshed for user: " + username);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String username) {
        authService.deleteToken("access:" + username);
        authService.deleteToken("refresh:" + username);

        kafkaTemplate.send("auth-events", "User logged out: " + username);

        return ResponseEntity.ok("Logged out successfully");
    }
}
