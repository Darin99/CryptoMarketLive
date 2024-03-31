package com.example.cryptomarketlive.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NavigableSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {

    private OrderBook orderBook;
    private final String pair = "XBT/USD";
    private NavigableSet<Order> testAsks;
    private NavigableSet<Order> testBids;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook();

        testAsks = new TreeSet<>();
        testAsks.add(new Order(100.0, 1.0));
        testAsks.add(new Order(101.0, 1.5));

        testBids = new TreeSet<>();
        testBids.add(new Order(99.0, 2.0));
        testBids.add(new Order(98.0, 3.0));
    }

    @Test
    void initializeDataForPairShouldCreateEmptyOrderSets() {
        orderBook.initializeDataForPair(pair);
        assertTrue(orderBook.getAsks().containsKey(pair));
        assertTrue(orderBook.getBids().containsKey(pair));
        assertTrue(orderBook.getAsks().get(pair).isEmpty());
        assertTrue(orderBook.getBids().get(pair).isEmpty());
    }

    @Test
    void updateOrderBookShouldUpdateTheAsksAndBids() {
        orderBook.initializeDataForPair(pair);
        orderBook.updateOrderBook(pair, testAsks, testBids);

        assertEquals(testAsks, orderBook.getAsks().get(pair));
        assertEquals(testBids, orderBook.getBids().get(pair));
    }

    @Test
    void toStringShouldReturnFormattedString() {
        orderBook.initializeDataForPair(pair);
        orderBook.updateOrderBook(pair, testAsks, testBids);

        String result = orderBook.toString(pair);

        assertNotNull(result);
        assertTrue(result.contains("Best Ask: [ 100.0, 1.0 ]"));
        assertTrue(result.contains("Best Bid: [ 99.0, 2.0 ]"));
        assertTrue(result.contains("BTC/USD"));
    }
}