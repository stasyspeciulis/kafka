package javatechtask.kafka.service.customer;

import java.util.List;

import javatechtask.kafka.service.CustomerInfo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerResource {
	private List<CustomerInfo> customers;
	private String status;
}
