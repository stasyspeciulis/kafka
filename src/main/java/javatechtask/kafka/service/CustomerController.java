package javatechtask.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.confluent.ksql.api.client.Row;
import javatechtask.kafka.ksqldb.BankCustomersRepository;
import javatechtask.kafka.ksqldb.KsqlConverter;

@Component
public class CustomerController {
	@Autowired
	BankCustomersRepository repo;

	public CustomerInfo getById(Integer id) {
		Row row = repo.findById(id);
		if (row == null) {
			return null;
		}
		return KsqlConverter.toCustomerInfo(row);
	}


}
