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

public class ProductsCsvDataProcessor implements DataProcessor<ProductData> {
	private static Logger log = LoggerFactory.getLogger(ProductsCsvDataProcessor.class);
	private static String ID = "productId";
	private static String NAME = "productName";
	private static String ATTR1 = "productAttr1";
	private static String ATTR2 = "productAttr2";
	private static List<String> fields = List.of(ID, NAME, ATTR1, ATTR2);

	private File file;
	private String fieldsSeparator;
	private Charset charset;

	public ProductsCsvDataProcessor(File file, String fieldsSeparator, Charset charset) {
		super();
		this.file = file;
		this.fieldsSeparator = fieldsSeparator;
		this.charset = charset;
	}

	@Override
	public List<ProductData> process() {
		// Get all text lines from one file
		List<String> lines = FileManager.readLines(file, charset);
		if (lines == null || lines.isEmpty()) {
			log.error("File {} has no lines", file.getPath());
			return null;
		}

		// Create products from each line
		List<ProductData> products = readProducts(lines);
		if (products == null || products.isEmpty()) {
			log.error("File {} has no products", file.getPath());
			return null;
		}

		return products;
	}

	private List<ProductData> readProducts(List<String> lines) {
		List<ProductData> result = new ArrayList<>();
		int i = 0;
		for (String line : lines) {
			i++;
			ProductData product = toProduct(line);
			if (product != null) {
				result.add(product);
			} else {
				log.error("Unable to read product: file {}, line {}", file.getPath(), i);
			}
		}
		return result;
	}

	private ProductData toProduct(String line) {
		// convert to a more comfortable structure
		List<String> values = toValues(line);
		if (values == null || values.isEmpty()) {
			log.error("Unable to find values in the line");
		}

		ProductData product = toProduct(values);
		return product;
	}

	private ProductData toProduct(List<String> values) {
		// convert to a more comfortable structure
		Map<String, String> map = toMap(values);
		if (map == null || map.isEmpty()) {
			log.error("Unable to map values to fields");
			return null;
		}

		// validate ID (mandatory, integer)
		if (map.get(ID) == null ||
				map.get(ID).isEmpty()) {
			log.error("Field {} value not specified", ID);
			return null;
		}
		Integer id = null;
		try {
			id = Integer.valueOf(map.get(ID));
		} catch (NumberFormatException e) {
			log.error("Unable to convert productId value [{}] to number", map.get(ID));
			return null;
		}

		ProductData result = new ProductData(
				id,
				map.get(NAME),
				map.get(ATTR1),
				map.get(ATTR2));

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
		List<String> values = Arrays.stream(line.split(fieldsSeparator))
				.map(String::trim)
				.collect(Collectors.toList());
		return values;
	}
}
