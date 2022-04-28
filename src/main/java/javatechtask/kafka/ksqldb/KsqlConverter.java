package javatechtask.kafka.ksqldb;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.confluent.ksql.api.client.KsqlArray;
import io.confluent.ksql.api.client.Row;
import javatechtask.kafka.service.CustomerInfo;
import javatechtask.kafka.service.TopCustomer;

public class KsqlConverter {
	public static List<Integer> toList(KsqlArray value) {
		if (value == null) {
			return null;
		}

		List<Integer> result = new ArrayList<>();

		List<?> tmp = value.getList();
		Iterator<?> iterator = tmp.iterator();

		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o == null) {
				continue;
			}
			if (o instanceof Integer) {
				Integer i = (Integer) o;
				result.add(i);
			} else if (o instanceof String) {
				String s = (String) o;
				Integer i = Integer.valueOf(s);
				result.add(i);
			} else {
				throw new RuntimeException("Unable to convert to Integer: [" + o.toString() + "] (" + o.getClass().getName() + ")");
			}
		}

		return result;
	}

	public static CustomerInfo toCustomerInfo(Row row) {
		if (row == null) {
			return null;
		}
		CustomerInfo result = new CustomerInfo(
				row.getInteger("CUSTOMERID"),
				row.getString("CUSTOMERNAME"),
				row.getString("CUSTOMERATTR1"),
				row.getString("CUSTOMERATTR2"),
				KsqlConverter.toList(row.getKsqlArray("PRODUCTS")));
		return result;
	}

	public static List<TopCustomer> toTopCustomers(List<Row> rows) {
		List<TopCustomer> result = new ArrayList<>();
		for (Row row : rows) {
			if (row != null) {
				TopCustomer topCustomer = toTopCustomer(row);
				if (topCustomer != null) {
					result.add(topCustomer);
				}
			}
		}
		return result;
	}

	private static TopCustomer toTopCustomer(Row row) {
		if (row == null) {
			return null;
		}
		TopCustomer result = new TopCustomer(
				new Date(row.getLong("ROWTIME")),
				row.getInteger("CUSTOMERID"),
				row.getString("CUSTOMERNAME"),
				row.getString("CUSTOMERATTR1"),
				row.getString("CUSTOMERATTR2"),
				KsqlConverter.toList(row.getKsqlArray("PRODUCTS")));
		return result;
	}
}
