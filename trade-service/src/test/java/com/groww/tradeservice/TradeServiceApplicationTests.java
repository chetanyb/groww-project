package com.groww.tradeservice;

import com.groww.tradeservice.exception.BadRequestException;
import com.groww.tradeservice.model.TradeRequest;
import com.groww.tradeservice.service.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class TradeServiceTest {

	private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("test")
			.withUsername("test_user")
			.withPassword("test_password");

	private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

	static {
		mysql.start();
		kafka.start();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Autowired
	private TradeService tradeService;

	@Test
	void testNullTradeRequest() {
		assertThrows(BadRequestException.class, () -> {
			tradeService.processTrade(null);
		});
	}

	@Test
	void testNullQuantity() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setQuantity(null);

		assertThrows(BadRequestException.class, () -> {
			tradeService.processTrade(tradeRequest);
		});
	}

	@Test
	void testNullTradeType() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setTradeType(null);

		assertThrows(BadRequestException.class, () -> {
			tradeService.processTrade(tradeRequest);
		});
	}

	@Test
	void testNullStockId() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setStockId(null);

		assertThrows(BadRequestException.class, () -> {
			tradeService.processTrade(tradeRequest);
		});
	}

	@Test
	void testInvalidQuantity() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setQuantity(-5);

		assertThrows(BadRequestException.class, () -> {
			tradeService.processTrade(tradeRequest);
		});
	}

	@Test
	void testInvalidTradeType() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setTradeType("INVALID");

		assertThrows(BadRequestException.class, () -> {
			tradeService.processTrade(tradeRequest);
		});
	}

	@Test
	void testInvalidStockId() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setStockId(-1L);

		assertThrows(BadRequestException.class, () -> {
			tradeService.processTrade(tradeRequest);
		});
	}

	@Test
	void testSuccessfulBuyTrade() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setQuantity(10);
		tradeRequest.setTradeType("BUY");
		tradeRequest.setStockId(1L);

		String result = tradeService.processTrade(tradeRequest);

		assertEquals("Trade successfully recorded!", result);
	}

	@Test
	void testSuccessfulSellTrade() {
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setQuantity(10);
		tradeRequest.setTradeType("SELL");
		tradeRequest.setStockId(1L);

		String result = tradeService.processTrade(tradeRequest);

		assertEquals("Trade successfully recorded!", result);
	}
}
