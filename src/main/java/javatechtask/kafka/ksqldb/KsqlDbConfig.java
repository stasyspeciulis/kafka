package javatechtask.kafka.ksqldb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.confluent.ksql.api.client.ClientOptions;

@Configuration
public class KsqlDbConfig {
	@Value("${ksqldb.server.host}")
	String host;
	@Value("${ksqldb.server.port}")
	String port;

	@Bean
	public ClientOptions clientOptions() {
		ClientOptions options = ClientOptions.create()
				.setHost(host)
				.setPort(Integer.valueOf(port));
		return options;
	}
}
