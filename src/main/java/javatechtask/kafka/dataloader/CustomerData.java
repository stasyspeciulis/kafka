package javatechtask.kafka.dataloader;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerData implements DataObject {
	private int customerId;
	private String customerName;
	private String customerAttr1;
	private String customerAttr2;
	private List<Integer> products;
}
