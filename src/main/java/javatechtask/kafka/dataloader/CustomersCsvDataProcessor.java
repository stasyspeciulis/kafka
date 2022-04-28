package javatechtask.kafka.dataloader;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomersCsvDataProcessor implements DataProcessor<CustomerData> {
	private static Logger log = LoggerFactory.getLogger(CustomersCsvDataProcessor.class);
	private static String ID = "customerId";
	private static String NAME = "customerName";
	private static String ATTR1 = "customerAttr1";
	private static String ATTR2 = "customerAttr2";
	private static String PRODUCTS = "products";
	private static List<String> fields = List.of(ID, NAME, ATTR1, ATTR2, PRODUCTS);

	private File file;
	private String fieldsSeparator;
	private Charset charset;

	public CustomersCsvDataProcessor(File file, String fieldsSeparator, Charset charset) {
		super();
		this.file = file;
		this.fieldsSeparator = fieldsSeparator;
		this.charset = charset;
	}

	@Override
	public List<CustomerData> process() {
		// Get all text lines from one file
		List<String> lines = FileManager.readLines(file, charset);
		if (lines == null || lines.isEmpty()) {
			log.error("File {} has no lines", file.getPath());
			return null;
		}

		// Create customers from each line
		List<CustomerData> customers = readCustomers(lines);
		if (customers == null || customers.isEmpty()) {
			log.error("File {} has no customers", file.getPath());
			return null;
		}

		return customers;
	}

	private List<CustomerData> readCustomers(List<String> lines) {
		List<CustomerData> result = new ArrayList<>();
		int i = 0;
		for (String line : lines) {
			i++;
			CustomerData Customer = toCustomer(line);
			if (Customer != null) {
				result.add(Customer);
			} else {
				log.error("Unable to read Customer: file {}, line {}", file.getPath(), i);
			}
		}
		return result;
	}

	private CustomerData toCustomer(String line) {
		// convert to a more comfortable structure
		List<String> values = toValues(line);
		if (values == null || values.isEmpty()) {
			log.error("Unable to find values in the line");
		}

		CustomerData Customer = toCustomer(values);
		return Customer;
	}

	private CustomerData toCustomer(List<String> values) {
		// convert to a more comfortable structure
		Map<String, String> map = toMap(values);
		if (map == null || map.isEmpty()) {
			log.error("Unable to map values to fields");
			return null;
		}

		// validate ID (mandatory, integer)
		if (map.get(ID) == null || map.get(ID).isEmpty()) {
			log.error("Field {} value not specified", ID);
			return null;
		}
		Integer id = null;
		try {
			id = Integer.valueOf(map.get(ID));
		} catch (NumberFormatException e) {
			log.error("Unable to convert customerId value [{}] to number", map.get(ID));
			return null;
		}


		// validate PRODUCTS (list of integers)
		List<Integer> products = null;
		try {
			products = toListOfIntegers(map.get(PRODUCTS));
		} catch (Exception e) {
			log.error("Unable to convert [{}] to a list of integers", map.get(PRODUCTS));
			log.error("{}", e);
			return null;
		}

		CustomerData result = new CustomerData(
				id,
				map.get(NAME),
				map.get(ATTR1),
				map.get(ATTR2),
				products);

		return result;
	}

	private List<Integer> toListOfIntegers(String value) {
		List<Integer> result = new ArrayList<>();
		if (value != null && !value.isEmpty()) {
			List<String> list = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
			for (String item : list) {
				if (item != null && !item.isEmpty()) {
					Integer i = Integer.valueOf(item);
					result.add(i);
				}
			}
		}
		return result;
	}

	private Map<String, String> toMap(List<String> values) {
		Map<String, String> map = new HashMap<>();
		for (String field : fields) {
			int index = fields.indexOf(field);
			if (index < values.size()) {
				map.put(field, values.get(index));
			}
		}
		return map;
	}

	private List<String> toValues(String line) {
		List<String> values = Arrays.stream(line.split(fieldsSeparator)).map(String::trim).collect(Collectors.toList());
		return values;
	}

}
