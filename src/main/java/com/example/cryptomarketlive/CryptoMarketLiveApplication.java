package com.example.cryptomarketlive;

import com.example.cryptomarketlive.handler.KrakenWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class CryptoMarketLiveApplication implements ApplicationRunner {

    private final KrakenWebSocketHandler handler;

    public static void main(String[] args) {
        SpringApplication.run(CryptoMarketLiveApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        handler.startClient();
    }
}