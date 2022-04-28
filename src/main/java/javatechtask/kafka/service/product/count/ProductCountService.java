package javatechtask.kafka.service.product.count;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javatechtask.kafka.service.ProductCountController;

@RestController
public class ProductCountService {
	private Logger log = LoggerFactory.getLogger(ProductCountService.class);

	@Autowired
	ProductCountController controller;

	@GetMapping("/product/count")
	public ProductCountResource getProductsCount() {
		ProductCountResource result = new ProductCountResource();
		try {
			Integer count = controller.getProductsCount();
			if (count != null) {
				result.setProductsCount(count);
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
