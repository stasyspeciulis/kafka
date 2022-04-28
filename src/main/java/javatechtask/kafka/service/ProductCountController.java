package javatechtask.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javatechtask.kafka.ksqldb.BankProductsRepository;

@Component
public class ProductCountController {
	@Autowired
	BankProductsRepository repo;

	public Integer getProductsCount() {
		Integer result = repo.getProductsCount();
		return result;
	}

}
