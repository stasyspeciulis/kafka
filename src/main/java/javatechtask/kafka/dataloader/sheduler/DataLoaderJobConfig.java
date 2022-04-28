package javatechtask.kafka.dataloader.sheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javatechtask.kafka.dataloader.DataLoader;

@Configuration
@EnableScheduling
public class DataLoaderJobConfig {
	private Logger log = LoggerFactory.getLogger(DataLoaderJobConfig.class);

	@Autowired
	DataLoader dataLoader;

	@Scheduled(cron = "${dataloader.scheduler.cron}")
	public void dataLoaderJob() {
		log.info("Dataloader job started");

		try {
			dataLoader.load();
		} catch (Exception e) {
			log.error("Data load job error", e);
		}

		log.info("Dataloader job finished");
	}
}
