package pl.app.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    //-------------------------------------JSON---------------------------------------------------------------------------
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerBaseObjectContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(baseObjectConsumerFactory());
        factory.getContainerProperties().setIdleBetweenPolls(7123);
        factory.getContainerProperties().setConsumerStartTimeout(Duration.ofMillis(7123));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Object> baseObjectConsumerFactory() {
        Map<String, Object> baseConsumerConfig = baseConsumerConfig();
        baseConsumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        baseConsumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>(); // there is need to add trusted packages
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(baseConsumerConfig, new StringDeserializer(), jsonDeserializer);
    }

    //-------------------------------------STRING---------------------------------------------------------------------------
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerBaseStringContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(baseStringConsumerFactory() );
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> baseStringConsumerFactory() {
        Map<String, Object> baseConsumerConfig = baseConsumerConfig();
        baseConsumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        baseConsumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(baseConsumerConfig);
    }

    //-------------------------------------CONFIG---------------------------------------------------------------------------
    @Bean
    public Map<String, Object> baseConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        return props;
    }

}
