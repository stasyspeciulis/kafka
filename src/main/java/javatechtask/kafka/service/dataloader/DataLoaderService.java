package javatechtask.kafka.service.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javatechtask.kafka.dataloader.DataLoader;

@RestController
public class DataLoaderService {
	private Logger log = LoggerFactory.getLogger(DataLoaderService.class);

	@Autowired
	DataLoader dataLoader;

	@GetMapping("/dataloader")
	public DataLoaderResource load() {
		try {
			dataLoader.load();
			return new DataLoaderResource("OK");
		} catch (Exception e) {
			log.error("Data load error", e);
			return new DataLoaderResource("ERROR");
		}
	}
}
