package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.app.config.KafkaConfig;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;
import pl.app.thread.application.service.ThreadService;

@Component
@RequiredArgsConstructor
@KafkaListener(id = "multi",
        containerFactory = "kafkaListenerBaseObjectContainerFactory",
        topics = {
                KafkaConfig.THREAD_LIST_TO_FETCH_TOPIC_NAME,
                KafkaConfig.THREAD_TO_FETCH_TOPIC_NAME,
                KafkaConfig.THREAD_WITH_LIST_TO_FETCH_TOPIC_NAME
        }
)
public class ThreadKafkaListener {
    private final Logger logger = LoggerFactory.getLogger(ThreadKafkaListener.class);
    private final ThreadService threadService;

    @KafkaHandler
    public void listen(ConsumerRecord<String, ThreadListToFetchMessage> cr, @Payload ThreadListToFetchMessage payload) {
        logger.info("Logger [ThreadListToFetchMessage-json] received key {} | Payload: {} | Record: {}", cr.key(), payload, cr.toString());
        threadService.fetchThreadListAndDelegateAllToKafka(payload);
    }

    @KafkaHandler
    public void listen(ConsumerRecord<String, ThreadToFetchMessage> cr, @Payload ThreadToFetchMessage payload) {
        logger.info("Logger [ThreadToFetchMessage-json] received key {} | Payload: {} | Record: {}", cr.key(), payload, cr.toString());
        threadService.fetchAndSaveThread(payload);
    }

    @KafkaHandler
    public void listen(ConsumerRecord<String, ThreadWithListToFetchMessage> cr, @Payload ThreadWithListToFetchMessage payload) {
        logger.info("Logger [ThreadWithListToFetchMessage-json] received key {} | Payload: {} | Record: {}", cr.key(), payload, cr.toString());
        threadService.fetchThreadWithListAndDelegateNotFetchedToKafka(payload);
    }
}
