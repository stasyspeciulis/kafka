package javatechtask.kafka.converter;

import org.springframework.stereotype.Component;

import javatechtask.kafka.avro.schemas.Product;
import javatechtask.kafka.dataloader.DataObject;
import javatechtask.kafka.dataloader.ProductData;

@Component
public class ProductDataConverter implements DataConverter {
	@Override
	public Object toAvroObject(DataObject o) {
		ProductData d = (ProductData) o;
		Product result = new Product();
		result.setProductId(d.getProductId());
		result.setProductName(d.getProductName());
		result.setProductAttr1(d.getProductAttr1());
		result.setProductAttr2(d.getProductAttr2());
		return result;
	}
}
