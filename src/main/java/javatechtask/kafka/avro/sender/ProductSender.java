package javatechtask.kafka.avro.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javatechtask.kafka.avro.schemas.Product;

@Component
public class ProductSender implements AvroSender {
	@Value("${kafka.topic.products.name}")
	private String topic;

	@Autowired
	@Qualifier("bankProductsKafkaTemplate")
	private KafkaTemplate<Integer, Product> kafkaTemplate;

	@Override
	public void sendObject(Object object) {
		Product v = (Product) object;
		kafkaTemplate.send(topic, getKey(v), isEmpty(v) ? new Product() : v);
	}

	private Integer getKey(Product product) {
		return Integer.valueOf(Integer.valueOf(product.getProductId()));
	}

	private boolean isEmpty(Product v) {
		if (v.getProductName() == null ||
				v.getProductName().isEmpty()) {
			return true;
		}
		return false;
	}
}
