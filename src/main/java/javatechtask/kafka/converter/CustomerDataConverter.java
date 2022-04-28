package javatechtask.kafka.converter;

import org.springframework.stereotype.Component;

import javatechtask.kafka.avro.schemas.Customer;
import javatechtask.kafka.dataloader.CustomerData;
import javatechtask.kafka.dataloader.DataObject;

@Component
public class CustomerDataConverter implements DataConverter {

	@Override
	public Object toAvroObject(DataObject o) {
		CustomerData d = (CustomerData) o;
		Customer result = new Customer();
		result.setCustomerId(d.getCustomerId());
		result.setCustomerName(d.getCustomerName());
		result.setCustomerAttr1(d.getCustomerAttr1());
		result.setCustomerAttr2(d.getCustomerAttr2());
		result.setProducts(d.getProducts());
		return result;
	}

}
