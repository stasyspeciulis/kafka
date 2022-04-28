package javatechtask.kafka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.confluent.ksql.api.client.Row;
import javatechtask.kafka.ksqldb.BankCustomersRepository;
import javatechtask.kafka.ksqldb.KsqlConverter;

@Component
public class TopCustomerController {
	@Autowired
	BankCustomersRepository repo;

	public List<TopCustomer> getTopCustomers(Integer n) {
		List<Row> rows = repo.getTopCustomers(n);
		if (rows == null || rows.isEmpty()) {
			return null;
		}
		return KsqlConverter.toTopCustomers(rows);
	}
}
