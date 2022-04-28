package javatechtask.kafka.dataloader;

import java.util.List;

public interface DataProcessor<T> {
	List<T> process();
}
