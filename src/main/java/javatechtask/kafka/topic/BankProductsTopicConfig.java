package javatechtask.kafka.topic;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import javatechtask.kafka.avro.schemas.Product;

@Configuration
public class BankProductsTopicConfig {

	@Value("${kafka.topic.products.partitions-num}")
	private Integer numPartitions;
	@Value("${kafka.topic.products.replication-factor}")
	private Short replicationFactor;

	@Autowired
	private ProducerFactory<String, String> global;

	@Bean("bankProductsKafkaTemplate")
	public KafkaTemplate<Integer, Product> kafkaTemplate() {
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(getConfig()));
	}

	private Map<String, Object> getConfig() {
		// Inherit configuration from Spring managed ProducerFactory
		Map<String, Object> properties = new HashMap<>(global.getConfigurationProperties());

		// Add specific configuration
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		return properties;
	}

}
