package javatechtask.kafka.ksqldb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import io.confluent.ksql.api.client.Row;

@Component
public class BankCustomersRepository {
	private static final String SQL_FIND_BY_ID = "select customerId, customerName, customerAttr1, customerAttr2, products "
			+
			"from bank_customers_table where customerId = %s limit 1;";
	private static final String SQL_TOP = "select rowtime, customerId, customerName, customerAttr1, customerAttr2, products "
			+
			"from bank_customers_table where rowtime > %s;";
	private static final String SQL_CUSTOMERS_COUNT = "select count(*) from bank_customers_table group by 1 emit changes limit 1;";

	@Autowired
	ClientOptions clientOptions;

	@Value("${service.customer-top.history-window-days}")
	Integer historyWindowDays;

	public Row findById(Integer id) {
		try (Client client = Client.create(clientOptions)) {
			String sql = String.format(SQL_FIND_BY_ID, id);
			BatchedQueryResult b = client.executeQuery(sql, getQueryProperties());
			List<Row> rows;
			try {
				rows = b.get();
			} catch (Exception e) {
				throw new RuntimeException("Unable to get customers by id: " + id, e);
			}

			if (rows == null || rows.isEmpty()) {
				return null;
			}

			for (Row row : rows) {
				return row;
			}
		}

		return null;
	}

	public Integer getCustomersCount() {
		try (Client client = Client.create(clientOptions)) {
			BatchedQueryResult b = client.executeQuery(SQL_CUSTOMERS_COUNT, getQueryProperties());
			List<Row> rows;
			try {
				rows = b.get();
			} catch (Exception e) {
				throw new RuntimeException("Unable to get customers count", e);
			}

			if (rows == null || rows.isEmpty()) {
				return 0;
			}

			for (Row row : rows) {
				return row.getInteger(1);
			}
		}

		return null;
	}

	public static Map<String, Object> getQueryProperties() {
		Map<String, Object> result = new HashMap<>();
		result.put("auto.offset.reset", "earliest");
		return result;
	}

	public List<Row> getTopCustomers(Integer n) {
		if (n == null || n < 1) {
			return null;
		}

		List<Row> result = new ArrayList<>();

		try (Client client = Client.create(clientOptions)) {
			String sql = String.format(SQL_TOP, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(historyWindowDays));
			BatchedQueryResult b = client.executeQuery(sql, getQueryProperties());
			List<Row> rows;
			try {
				rows = b.get();
			} catch (Exception e) {
				throw new RuntimeException("Unable to get top customers", e);
			}

			if (rows == null || rows.isEmpty()) {
				return null;
			}

			// Get list iterator from last element + 1
			ListIterator<Row> iterator = rows.listIterator(rows.size());
			while (iterator.hasPrevious()) {
				result.add(iterator.previous());

				// Gather only N elements
				if (result.size() == n) {
					break;
				}
			}
		}

		return result;
	}
}
