package pl.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerStringConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerBaseStringContainerFactory(
//            ConsumerFactory<String, String> baseStringConsumerFactory
//    ) {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(baseStringConsumerFactory);
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, String> baseStringConsumerFactory(
//            Map<String, Object> myBaseConsumerConfig
//    ) {
//        myBaseConsumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        myBaseConsumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        return new DefaultKafkaConsumerFactory<>(myBaseConsumerConfig);
//    }

}
