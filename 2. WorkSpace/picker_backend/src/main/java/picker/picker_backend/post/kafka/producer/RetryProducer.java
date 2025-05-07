package picker.picker_backend.post.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import picker.picker_backend.post.config.PostProperties;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class RetryProducer {

    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    protected PostProperties postProperties;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public int retrySendMessage(Object postDTO, String eventType, int attempt){
        //String topic = postProperties.getKafka().getTopic();
        String topic = "post-events";
        //int maxRetries = postProperties.getKafka().getMaxRetries();
        int maxRetries = 3;
        long baseDelay = 1000;
        long delayMs = (long)Math.pow(2, attempt - 1) * baseDelay;

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, eventType, postDTO);

        return future.thenApply(result -> {
            return 1;
        }).exceptionally(ex -> {
            if(attempt < maxRetries){
                scheduledExecutorService.schedule(()->{
                    retrySendMessage(postDTO,eventType,attempt+1);
                }, delayMs, TimeUnit.MICROSECONDS);
                return null;
            } else{
                return 0;
            }
        }).join();
    }

}
