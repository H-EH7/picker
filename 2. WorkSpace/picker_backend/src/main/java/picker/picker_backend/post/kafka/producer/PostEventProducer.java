package picker.picker_backend.post.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.config.PostProperties;
import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;

import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class PostEventProducer extends RetryProducer{

    private final KafkaTemplate<String, PostInsertDTO> kafkaTemplate;
    private final PostProperties postProperties;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            3,
            10,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(100)
    );

    private <T> void sendEventAsync(T postDTO, String eventType, CompletableFuture<Integer> resultFuture){
        executorService.submit(()->{
           int result = retrySendMessage(postDTO, eventType, 1);
           resultFuture.complete(result);
        });

    }

    public CompletableFuture<Integer> sendPostInsertEvent(PostInsertDTO postInsertDTO){
        CompletableFuture<Integer> resultFuture = new CompletableFuture<>();
        sendEventAsync(postInsertDTO, "insert", resultFuture);
        return resultFuture;
    }

    public CompletableFuture<Integer> sendPostUpdateEvent(PostUpdateDTO postUpdateDTO){
        CompletableFuture<Integer> resultFuture = new CompletableFuture<>();
        sendEventAsync(postUpdateDTO, "update", resultFuture);
        return resultFuture;
    }

}
