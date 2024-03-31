package com.example.cryptomarketlive.config;

import com.example.cryptomarketlive.entity.OrderBook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBookConfig {

    @Bean
    public OrderBook orderBook() {
        return new OrderBook();
    }
}
