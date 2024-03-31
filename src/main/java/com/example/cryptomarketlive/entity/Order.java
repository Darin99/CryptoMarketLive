package com.example.cryptomarketlive.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Order implements Comparable<Order> {
    private final double price;
    private final double amount;

    @Override
    public int compareTo(Order o) {
        return Double.compare(this.price, o.price);
    }

    @Override
    public String toString() {
        return "[ " + price + ", " + amount + " ]";
    }
}