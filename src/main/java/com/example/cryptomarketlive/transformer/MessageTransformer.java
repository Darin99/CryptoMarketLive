package com.example.cryptomarketlive.transformer;

import com.example.cryptomarketlive.entity.Order;
import com.example.cryptomarketlive.entity.OrderBook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NavigableSet;

@Component
@RequiredArgsConstructor
public class MessageTransformer {
    private final ObjectMapper objectMapper;
    private final OrderBook orderBook;

    public void transformMessage(String messagePayload) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(messagePayload);

        if (jsonNode.isArray()) {
            JsonNode payload = jsonNode.get(1);
            String pair = jsonNode.get(3).asText();

            orderBook.initializeDataForPair(pair);

            NavigableSet<Order> asks = orderBook.getAsks().get(pair);
            NavigableSet<Order> bids = orderBook.getBids().get(pair);

            processOrders(payload, new String[]{"a", "as"}, asks);
            processOrders(payload, new String[]{"b", "bs"}, bids);
            orderBook.updateOrderBook(pair, asks, bids);
            System.out.println(orderBook.toString(pair));
        }
    }

    private void processOrders(JsonNode payload, String[] keys, NavigableSet<Order> orders) {
        for (String key : keys) {
            if (payload.has(key)) {
                addOrders(payload.get(key), orders);
            }
        }
    }

    private void addOrders(JsonNode orderNodes, NavigableSet<Order> orders) {
        for (JsonNode currentNode : orderNodes) {
            double price = currentNode.get(0).asDouble();
            double volume = currentNode.get(1).asDouble();
            orders.add(new Order(price, volume));
        }
    }
}