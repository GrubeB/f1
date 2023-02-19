package pl.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {
    public static final String THREAD_TO_FETCH_TOPIC_NAME = "threadToFetch";
    public static final String THREAD_WITH_LIST_TO_FETCH_TOPIC_NAME = "threadWithListToFetch";
    public static final String THREAD_LIST_TO_FETCH_TOPIC_NAME = "threadListToFetch";

    @Bean
    public NewTopic threadToFetchTopic() {
        return TopicBuilder
                .name(THREAD_TO_FETCH_TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic threadWithListToFetchTopic() {
        return TopicBuilder
                .name(THREAD_WITH_LIST_TO_FETCH_TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic threadListToFetchTopic() {
        return TopicBuilder
                .name(THREAD_LIST_TO_FETCH_TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
