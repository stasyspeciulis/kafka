package javatechtask.kafka.service.customer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javatechtask.kafka.service.CustomerController;
import javatechtask.kafka.service.CustomerInfo;

@RestController
public class CustomerService {
	private Logger log = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	CustomerController controller;

	@GetMapping("/customer")
	public CustomerResource getById(@RequestParam(name = "id") String id) {
		CustomerResource result = new CustomerResource();
		try {
			CustomerInfo info = controller.getById(Integer.valueOf(id));
			if (info != null) {
				result.setCustomers(List.of(info));
				result.setStatus("OK");
			} else {
				result.setStatus("NOT FOUND");
			}
			return result;
		} catch (Exception e) {
			log.error("Error in getById " + id, e);
			result.setStatus("ERROR");
			return result;
		}
	}

}
