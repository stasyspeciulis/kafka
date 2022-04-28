package javatechtask.kafka.producer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javatechtask.kafka.avro.sender.SenderFactory;
import javatechtask.kafka.converter.Converter;
import javatechtask.kafka.dataloader.DataObject;

@Component
public class Producer {
	Logger log = LoggerFactory.getLogger(Producer.class);

	@Autowired
	Converter converter;

	@Autowired
	SenderFactory senderFactory;

	public void send(List<? extends DataObject> data) {
		List<Object> objects = converter.convertToAvro(data);

		for (Object o : objects) {
			senderFactory.get(o).sendObject(o);
		}
	}
}
