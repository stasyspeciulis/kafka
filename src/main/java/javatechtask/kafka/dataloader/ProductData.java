package javatechtask.kafka.dataloader;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductData implements DataObject {
	private int productId;
	private String productName;
	private String productAttr1;
	private String productAttr2;
}
