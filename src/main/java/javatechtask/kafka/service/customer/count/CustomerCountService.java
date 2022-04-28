package javatechtask.kafka.service.customer.count;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javatechtask.kafka.service.CustomerCountController;

@RestController
public class CustomerCountService {
	private Logger log = LoggerFactory.getLogger(CustomerCountService.class);

	@Autowired
	CustomerCountController controller;

	@GetMapping("/customer/count")
	public CustomerCountResource getProductsCount() {
		CustomerCountResource result = new CustomerCountResource();
		try {
			Integer count = controller.getCustomersCount();
			if (count != null) {
				result.setCustomersCount(count);
				result.setStatus("OK");
			} else {
				result.setStatus("NOT FOUND");
			}
			return result;
		} catch (Exception e) {
			log.error("Error in getProductsCount", e);
			result.setStatus("ERROR");
			return result;
		}
	}

}
