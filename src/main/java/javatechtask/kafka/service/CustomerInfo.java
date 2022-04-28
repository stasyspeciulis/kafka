package javatechtask.kafka.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CustomerInfo {
	private Integer customerId;
	private String customerName;
	private String customerAttr1;
	private String customerAttr2;
	List<Integer> products;
}
