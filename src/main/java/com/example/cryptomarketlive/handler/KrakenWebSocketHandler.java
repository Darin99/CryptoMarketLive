package com.example.cryptomarketlive.handler;

import com.example.cryptomarketlive.transformer.MessageTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class KrakenWebSocketHandler extends TextWebSocketHandler {

    private static final String KRAKEN_WS_URL = "wss://ws.kraken.com";
    private static final String SUBSCRIBE_MESSAGE = "{\"event\":\"subscribe\", \"pair\":[\"BTC/USD\", \"ETH/USD\"], \"subscription\":{\"name\":\"book\"}}";

    private final MessageTransformer transformer;
    private final StandardWebSocketClient webSocketClient;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage(SUBSCRIBE_MESSAGE));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        transformer.transformMessage(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection closed: " + status);
    }

    public void startClient() throws Exception {
        webSocketClient.execute(this, KRAKEN_WS_URL).get();
    }
}