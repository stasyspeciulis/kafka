package javatechtask.kafka.dataloader;

import java.io.File;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javatechtask.kafka.producer.Producer;


@Component
public class DataLoader {
	Logger log = LoggerFactory.getLogger(DataLoader.class);

	@Autowired
	Producer producer;

	@Value("${dataloader.base.path}")
	String basePath;

	@Value("${dataloader.files.extension}")
	String filesExtension;

	@Value("${dataloader.fields.separator}")
	String fieldsSeparator;

	@Value("${dataloader.batch.date.pattern}")
	String batchDatePattern;

	@Value("${dataloader.files.product}")
	String filesProduct;

	@Value("${dataloader.files.customer}")
	String filesCustomer;

	@Value("${dataloader.files.charset}")
	String charset;

	public void load() {
		List<File> files = FileManager.prepareFiles(basePath, getDirectoryName(), getEndsWith(), getStartsWith());
		if (files != null && !files.isEmpty()) {
			processFiles(files);
		}
	}

	private void processFiles(List<File> list) {
		for (File item : list) {
			try {
				processFile(item);
			} catch (Exception e) {
				log.error("Unable to process file {}", item.getPath());
				log.error("{}", e);
			}
		}
	}

	private void processFile(File file) {
		log.info("Processing file {}", file.getPath());
		DataProcessor<? extends DataObject> dp = getDataProcessor(file, fieldsSeparator, Charset.forName(charset));
		if (dp != null) {
			List<? extends DataObject> list = dp.process();
			if (list != null && !list.isEmpty()) {
				producer.send(list);
			}
		}
	}

	private DataProcessor<? extends DataObject> getDataProcessor(File file, String fieldsSeparator, Charset charset) {
		if (isProductsCsv(file)) {
			return new ProductsCsvDataProcessor(file, fieldsSeparator, charset);
		} else if (isCustomersCsv(file)) {
			return new CustomersCsvDataProcessor(file, fieldsSeparator, charset);
		} else {
			log.error("Unable to find processor for the file {}", file.getPath());
		}
		return null;
	}

	private boolean isProductsCsv(File file) {
		return file.getName().toLowerCase().startsWith(filesProduct.toLowerCase());
	}

	private boolean isCustomersCsv(File file) {
		return file.getName().toLowerCase().startsWith(filesCustomer.toLowerCase());
	}

	public String getDirectoryName() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(batchDatePattern));
	}

	private List<String> getEndsWith() {
		List<String> result = new ArrayList<>();
		if (filesExtension != null && !filesExtension.isEmpty()) {
			result.add(filesExtension);
		}
		return result;
	}

	private List<String> getStartsWith() {
		List<String> result = new ArrayList<>();
		if (filesProduct != null && !filesProduct.isEmpty()) {
			result.add(filesProduct);
		}
		if (filesCustomer != null && !filesCustomer.isEmpty()) {
			result.add(filesCustomer);
		}
		return result;
	}
}
