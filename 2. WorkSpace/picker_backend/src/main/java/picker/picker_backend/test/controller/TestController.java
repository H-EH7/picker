package picker.picker_backend.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import picker.picker_backend.test.model.dto.TestResponseDto;
import picker.picker_backend.test.service.TestService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    @Autowired
    private TestService testService;

    /// Mapper 테스트 ======================================
    @GetMapping("/{id}")
    public TestResponseDto getTest(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    /// Redis 테스트 ======================================
    @PostMapping("/redis")
    public void save(@RequestParam String key, @RequestParam String value) {
        testService.save(key, value);
    }

    @GetMapping("/redis")
    public String get(@RequestParam String key) {
        return testService.get(key);
    }

    /// Kafka 테스트 ======================================
    @PostMapping("/kafka")
    public String send(@RequestParam String message) {
        testService.sendMessage("test-topic", message);
        return "메시지 전송됨: " + message;
    }
}
