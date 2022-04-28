package javatechtask.kafka.service.customer.top;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javatechtask.kafka.service.TopCustomer;
import javatechtask.kafka.service.TopCustomerController;

@RestController
public class TopCustomerService {
	private Logger log = LoggerFactory.getLogger(TopCustomerService.class);

	@Autowired
	TopCustomerController controller;

	@GetMapping("/customer/top")
	public TopCustomerResource getTopCustomers(@RequestParam(name = "n") String n) {
		TopCustomerResource result = new TopCustomerResource();
		try {
			List<TopCustomer> list = controller.getTopCustomers(Integer.valueOf(n));
			if (list != null) {
				result.setTopCustomers(list);
				result.setStatus("OK");
			} else {
				result.setStatus("NOT FOUND");
			}
			return result;
		} catch (Exception e) {
			log.error("Error in getTopCustomers " + n, e);
			result.setStatus("ERROR");
			return result;
		}
	}

}
