package javatechtask.kafka.avro.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javatechtask.kafka.avro.schemas.Customer;
import javatechtask.kafka.avro.schemas.Product;

@Component
public class SenderFactory {
	@Autowired
	private ApplicationContext context;

	public AvroSender get(Object o) {
		AvroSender result = null;
		if (o instanceof Customer) {
			result = context.getBean(CustomerSender.class);
		} else if (o instanceof Product) {
			result = context.getBean(ProductSender.class);
		} else {
			throw new RuntimeException("Unable to find sender for object " + o.getClass().getName());
		}
		return result;
	}

}
