package com.test.mizuho.dao;


/**
 * Suggestions for improvement:
 * - implement equals and hashcode method
 * - create two child classes for the Order. One for Bid and one Offer with predefined  side set and
 * each of them to implement Comparable interface
 * - implement set methods for size and price
 * - add a new timestamp field to recorde when the order was placed
 * - add additional fields such as market order, limit order, execution status to see how the orders
 * are executed in the market and under which conditions.
 *
 * */
public class Order {
    private long id;
    private double price;
    private char side;
    private long size;


    public Order(long id, double price, char side, long size) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }
}
