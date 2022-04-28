package javatechtask.kafka.ksqldb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import io.confluent.ksql.api.client.Row;

@Component
public class BankProductsRepository {
	private static final String SQL_PRODUCTS_COUNT = "select count(*) from bank_products_table group by 1 emit changes limit 1;";
	@Autowired
	ClientOptions clientOptions;

	public Integer getProductsCount() {
		try (Client client = Client.create(clientOptions)) {
			BatchedQueryResult b = client.executeQuery(SQL_PRODUCTS_COUNT, getQueryProperties());
			List<Row> rows;
			try {
				rows = b.get();
			} catch (Exception e) {
				throw new RuntimeException("Unable to get products count", e);
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
}
