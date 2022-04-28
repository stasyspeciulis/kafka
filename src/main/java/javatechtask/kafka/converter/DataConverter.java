package javatechtask.kafka.converter;

import javatechtask.kafka.dataloader.DataObject;

public interface DataConverter {
	Object toAvroObject(DataObject o);
}
