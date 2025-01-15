package com.groww.tradeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groww.tradeservice.model.TradeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        // Exclude security auto-configuration to prevent security-related context loading issues
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
@AutoConfigureMockMvc(addFilters = false)  // Disable security and other filters during tests
class TradeControllerTest {

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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Additional setup if needed
    }

    @Test
    void whenNullTradeRequest_thenBadRequest() throws Exception {
        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenInvalidQuantity_thenBadRequest() throws Exception {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserAccountId(1L);
        tradeRequest.setQuantity(-5);
        tradeRequest.setTradeType("BUY");
        tradeRequest.setStockId(1L);

        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$.quantity").value(containsString("must be positive")));
    }

    @Test
    void whenInvalidTradeType_thenBadRequest() throws Exception {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserAccountId(1L);
        tradeRequest.setQuantity(10);
        tradeRequest.setTradeType("NOT_BUY_OR_SELL");
        tradeRequest.setStockId(1L);

        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tradeType").exists())
                .andExpect(jsonPath("$.tradeType").value(containsString("Trade type must be BUY or SELL")));
    }

    @Test
    void whenValidBuyTradeRequest_thenOk() throws Exception {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserAccountId(1L);
        tradeRequest.setQuantity(10);
        tradeRequest.setTradeType("BUY");
        tradeRequest.setStockId(1L);

        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void whenValidSellTradeRequest_thenOk() throws Exception {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setUserAccountId(1L);
        tradeRequest.setQuantity(10);
        tradeRequest.setTradeType("SELL");
        tradeRequest.setStockId(1L);

        mockMvc.perform(post("/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeRequest)))
                .andExpect(status().isOk());
    }
}
