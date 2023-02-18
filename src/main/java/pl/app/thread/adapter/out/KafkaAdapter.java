package pl.app.thread.adapter.out;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import pl.app.config.KafkaConfig;
import pl.app.thread.application.port.in.dto.ThreadListToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadToFetchMessage;
import pl.app.thread.application.port.in.dto.ThreadWithListToFetchMessage;
import pl.app.thread.application.port.out.DelegateThreadListToFetchKafka;
import pl.app.thread.application.port.out.DelegateThreadToFetchKafka;
import pl.app.thread.application.port.out.DelegateThreadWithListToFetchKafka;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaAdapter implements
        DelegateThreadListToFetchKafka,
        DelegateThreadToFetchKafka,
        DelegateThreadWithListToFetchKafka {
    private final Logger logger = LoggerFactory.getLogger(KafkaAdapter.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendToKafka(final ThreadToFetchMessage data) {
        final ProducerRecord<String, Object> record = createRecord(data);
        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send(record);
        send.whenComplete(this::logHandler);
    }

    @Override
    public void sendToKafka(ThreadListToFetchMessage data) {
        final ProducerRecord<String, Object> record = createRecord(data);
        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send(record);
        send.whenComplete(this::logHandler);
    }

    @Override
    public void sendToKafka(ThreadWithListToFetchMessage data) {
        final ProducerRecord<String, Object> record = createRecord(data);
        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send(record);
        send.whenComplete(this::logHandler);
    }

    private void logHandler(SendResult<String, Object> result, Throwable ex) {
        if (ex == null) {
            logger.info("Successful sent record to kafka topic: " + result.getProducerRecord().topic());
        } else {
            logger.error("Failed to send record to kafka topic: " + result.getProducerRecord().topic());
        }
    }

    private ProducerRecord<String, Object> createRecord(ThreadListToFetchMessage data) {
        return new ProducerRecord<>(KafkaConfig.THREAD_LIST_TO_FETCH_TOPIC_NAME,"ThreadListToFetchMessage", data);
    }

    private ProducerRecord<String, Object> createRecord(ThreadToFetchMessage data) {
        return new ProducerRecord<>(KafkaConfig.THREAD_TO_FETCH_TOPIC_NAME,"ThreadToFetchMessage", data);
    }

    private ProducerRecord<String, Object> createRecord(ThreadWithListToFetchMessage data) {
        return new ProducerRecord<>(KafkaConfig.THREAD_WITH_LIST_TO_FETCH_TOPIC_NAME, "ThreadWithListToFetchMessage", data);
    }

}
