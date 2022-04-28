package javatechtask.kafka.converter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javatechtask.kafka.dataloader.CustomerData;
import javatechtask.kafka.dataloader.DataObject;
import javatechtask.kafka.dataloader.ProductData;

@Component
public class Converter {
	Logger log = LoggerFactory.getLogger(Converter.class);

	@Autowired
	private ApplicationContext context;

	public List<Object> convertToAvro(List<? extends DataObject> list) {
		List<Object> result = new ArrayList<>();

		for (DataObject item : list) {
			DataConverter c = getConverter(item);
			result.add(c.toAvroObject(item));
		}

		return result;
	}

	private DataConverter getConverter(DataObject o) {
		DataConverter result = null;
		if (o instanceof CustomerData) {
			result = context.getBean(CustomerDataConverter.class);
		} else if (o instanceof ProductData) {
			result = context.getBean(ProductDataConverter.class);
		} else {
			throw new RuntimeException("Unable to find converter for object " + o.getClass().getName());
		}
		return result;
	}

}
