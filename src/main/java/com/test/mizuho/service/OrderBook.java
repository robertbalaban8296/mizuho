package com.test.mizuho.service;

import com.test.mizuho.dao.Order;

import java.util.*;
import java.util.stream.Collectors;


/** Suggestions for improvement:
 * - I would create an interface, define the methods there and implement the interface in this class
 * - Usage of high efficient data structures
 * - Adding support for concurrency and multithreading for the methods
 * */
public class OrderBook {

    private final TreeMap<Double, PriorityQueue<Order>> bids = new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<Double, PriorityQueue<Order>> offers = new TreeMap<>(Comparator.naturalOrder());
    private final Map<Long, Order> orderMap = new HashMap<>();

    public Order add(Order order) {
        Double price = order.getPrice();
        orderMap.put(order.getId(), order);

        if (order.getSide() == 'B') {
            bids.computeIfAbsent(price, k -> new PriorityQueue<>((o1, o2) -> (int) (o2.getSize() - o1.getSize()))).add(order);
        } else if (order.getSide() == 'O') {
            offers.computeIfAbsent(price, k -> new PriorityQueue<>((o1, o2) -> (int) (o1.getSize() - o2.getSize()))).add(order);
        } else {
            return null;
        }
        return order;
    }

    public long remove(long orderId) {
        if (orderMap.containsKey(orderId) && orderMap.get(orderId) != null) {
            Order order = orderMap.remove(orderMap.get(orderId));
            TreeMap<Double, PriorityQueue<Order>> treeMap = order.getSide() == 'B' ? bids : offers;
            PriorityQueue<Order> orders = treeMap.get(order.getPrice());

            orders.remove(order);

            if (orders.isEmpty()) {
                orderMap.remove(order.getPrice());
            }

        }
        return orderId;
    }

    public void modifyOrderSize(long orderId, long newSize) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            remove(orderId);
            add(new Order(orderId, order.getPrice(), order.getSide(), newSize)); // because we don't have the setter defined
        }
    }

    public double getPriceByLevel(char side, int level) {
        Map<Double, PriorityQueue<Order>> orderMap = side == 'B' ? bids : offers;
        if (level > 0 && level <= orderMap.size()) {
            int levelCounter = 1;
            for (double price : orderMap.keySet()) {
                if (levelCounter == level) {
                    return price;
                }
                levelCounter++;
            }

        }
        return -1.0; // Level not available
    }

    public long getTotalSizeByLevel(char side, int level) {
        Map<Double, PriorityQueue<Order>> orderMap = side == 'B' ? bids : offers;
        if (level > 0 && level <= orderMap.size()) {
            int levelCounter = 1;
            for (PriorityQueue<Order> orders : orderMap.values()) {
                if (levelCounter == level) {
                    return orders.stream().mapToLong(Order::getSize).sum();
                }
                levelCounter++;
            }
        }

        return -1; // Level not available
    }

    public List<Order> getOrdersBySide(char side) {
        Map<Double, PriorityQueue<Order>> orderMap = side == 'B' ? bids : offers;
        return orderMap.values().stream().flatMap(PriorityQueue::stream).collect(Collectors.toList());
    }
}
