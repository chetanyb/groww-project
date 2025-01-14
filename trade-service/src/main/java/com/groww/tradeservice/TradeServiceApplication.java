package com.groww.tradeservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class TradeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeServiceApplication.class, args);
	}

	@Bean
	public NewTopic tradeConfirmationTopic() {
		return TopicBuilder.name("trade-confirmation")
				.partitions(1)
				.replicas(1)
				.build();
	}
}
