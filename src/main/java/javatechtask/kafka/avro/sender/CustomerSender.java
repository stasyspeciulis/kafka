package javatechtask.kafka.avro.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javatechtask.kafka.avro.schemas.Customer;

@Component
public class CustomerSender implements AvroSender {
	@Value("${kafka.topic.customers.name}")
	private String topic;

	@Autowired
	@Qualifier("bankCustomersKafkaTemplate")
	private KafkaTemplate<Integer, Customer> kafkaTemplate;

	@Override
	public void sendObject(Object object) {
		Customer v = (Customer) object;
		kafkaTemplate.send(topic, getKey(v), isEmpty(v) ? new Customer() : v);
	}

	private Integer getKey(Customer customer) {
		return Integer.valueOf(customer.getCustomerId());
	}

	private boolean isEmpty(Customer customer) {
		if (customer.getCustomerName() == null ||
				customer.getCustomerName().isEmpty()) {
			return true;
		}
		return false;
	}
}
