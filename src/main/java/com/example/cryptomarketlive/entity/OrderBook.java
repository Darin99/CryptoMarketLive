package com.example.cryptomarketlive.entity;


import lombok.Getter;

import java.time.Instant;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class OrderBook {

    private final ConcurrentHashMap<String, NavigableSet<Order>> asks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, NavigableSet<Order>> bids = new ConcurrentHashMap<>();

    public void initializeDataForPair(String pair) {
        asks.computeIfAbsent(pair, k -> new TreeSet<>());
        bids.computeIfAbsent(pair, k -> new TreeSet<>());
    }

    public void updateOrderBook(String pair,
                                NavigableSet<Order> newAsks,
                                NavigableSet<Order> newBids) {
        asks.put(pair, newAsks);
        bids.put(pair, newBids);
    }

    public String toString(String pair) {
        StringBuilder builder = new StringBuilder();
        builder.append("<----------------------->\n");

        builder.append("Asks:\n");
        appendOrders(builder, asks.get(pair).descendingSet());

        builder.append("Best Bid: ")
                .append(bids.get(pair).last())
                .append(System.lineSeparator());

        builder.append("Best Ask: ")
                .append(asks.get(pair).first())
                .append(System.lineSeparator());

        builder.append("Bids:\n");
        appendOrders(builder, bids.get(pair).descendingSet());

        builder.append(Instant.now().toString())
                .append(System.lineSeparator())
                .append("\nPair: ")
                .append(getDisplayPair(pair))
                .append(System.lineSeparator())
                .append(">-----------------------<\n");

        return builder.toString();
    }

    private void appendOrders(StringBuilder builder,
                              NavigableSet<Order> orders) {
        for (Order order : orders) {
            builder.append(order).append(System.lineSeparator());
        }
    }

    private String getDisplayPair(String pair) {
        if (pair.equals("XBT/USD")) {
            return "BTC/USD";
        }
        return pair;
    }
}