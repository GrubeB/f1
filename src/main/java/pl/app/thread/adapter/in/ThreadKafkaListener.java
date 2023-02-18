package pl.app.thread.adapter.in;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ThreadKafkaListener {
    private final Logger logger = LoggerFactory.getLogger(ThreadKafkaListener.class);
    private final ThreadService threadService;

    @KafkaListener(
            id = "1",
            clientIdPrefix = "ThreadListToFetchMessage-json",
            groupId = "ThreadListToFetchMessageGroupId",
            topicPartitions = {@TopicPartition(topic = KafkaConfig.THREAD_LIST_TO_FETCH_TOPIC_NAME, partitions = "0")},
            containerFactory = "kafkaListenerBaseObjectContainerFactory53123"
    )
    public void listenAsObject(ConsumerRecord<String, ThreadListToFetchMessage> cr, @Payload ThreadListToFetchMessage payload) {
        logger.info("Logger [ThreadListToFetchMessage-json] received key {} | Payload: {} | Record: {}", cr.key(), payload, cr.toString());
        threadService.fetchThreadListAndDelegateAllToKafka(payload);
    }

    @KafkaListener(
            id = "2",
            clientIdPrefix = "ThreadToFetchMessage-json",
            groupId = "ThreadToFetchMessageGroupId",
            topicPartitions = {@TopicPartition(topic = KafkaConfig.THREAD_TO_FETCH_TOPIC_NAME, partitions = "0")},
            containerFactory = "kafkaListenerBaseObjectContainerFactory7123"
    )
    public void listenAsObject(ConsumerRecord<String, ThreadToFetchMessage> cr, @Payload ThreadToFetchMessage payload) {
        logger.info("Logger [ThreadToFetchMessage-json] received key {} | Payload: {} | Record: {}", cr.key(), payload, cr.toString());
        threadService.fetchAndSaveThread(payload);
    }

    @KafkaListener(
            id = "3",
            clientIdPrefix = "ThreadWithListToFetchMessage-json",
            groupId = "ThreadWithListToFetchMessageGroupId",
            topicPartitions = {@TopicPartition(topic = KafkaConfig.THREAD_WITH_LIST_TO_FETCH_TOPIC_NAME, partitions = "0")},
            containerFactory = "kafkaListenerBaseObjectContainerFactory13123"
    )
    public void listenAsObject(ConsumerRecord<String, ThreadWithListToFetchMessage> cr, @Payload ThreadWithListToFetchMessage payload) {
        logger.info("Logger [ThreadWithListToFetchMessage-json] received key {} | Payload: {} | Record: {}", cr.key(), payload, cr.toString());
        threadService.fetchThreadWithListAndDelegateNotFetchedToKafka(payload);
    }
}
