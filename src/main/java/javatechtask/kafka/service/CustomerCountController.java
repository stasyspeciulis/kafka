package javatechtask.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javatechtask.kafka.ksqldb.BankCustomersRepository;

@Component
public class CustomerCountController {
	@Autowired
	BankCustomersRepository repo;

	public Integer getCustomersCount() {
		Integer result = repo.getCustomersCount();
		return result;
	}

}
