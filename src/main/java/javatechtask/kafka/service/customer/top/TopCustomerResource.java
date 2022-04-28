package javatechtask.kafka.service.customer.top;

import java.util.List;

import javatechtask.kafka.service.TopCustomer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopCustomerResource {
	List<TopCustomer> topCustomers;
	String status;
}
