package picker.picker_backend.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.test.model.dto.TestResponseDto;
import picker.picker_backend.test.test.TestService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    @Autowired
    private TestService testService;
    private final JobLauncher jobLauncher;
    private final Job postViewCountJob;
    private final Job postPopJob;
    private final PostRedisViewCountManager postRedisViewCountManager;
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


    @GetMapping("/batch")
    public String runBatch() throws Exception{

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

        JobExecution executionViewCount = jobLauncher.run(postViewCountJob,jobParameters);

        if(executionViewCount.getStatus() == BatchStatus.COMPLETED){

            JobExecution executionPop =  jobLauncher.run(postPopJob, jobParameters);

            if(executionPop.getStatus() == BatchStatus.COMPLETED){
                postRedisViewCountManager.initViewCount();
            }
        }
        return "aaaa";
    }
}
