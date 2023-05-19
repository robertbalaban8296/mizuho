package com.test.mizuho.service;

import com.test.mizuho.dao.Order;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderBookTest {

    private OrderBook orderBook = new OrderBook();
    private Order bid1 = new Order(1, 10.50, 'B', 9);
    private Order bid2 = new Order(2, 12.00, 'B', 10);
    private Order bid3 = new Order(3, 10.50, 'B', 15);
    private Order bid4 = new Order(4, 2, 'B', 3);


    private Order offer1 = new Order(5, 10.50, 'O', 4);
    private Order offer2 = new Order(6, 12.00, 'O', 10);
    private Order offer3 = new Order(7, 10.50, 'O', 2);
    private Order offer4 = new Order(8, 2, 'O', 3);

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("All orders were successfully added.")
    void successfulAddOrders() {
        // Add Bids
        orderBook.add(bid1);
        orderBook.add(bid2);
        orderBook.add(bid3);
        orderBook.add(bid4);

        List<Order> allBids = orderBook.getOrdersBySide('B');

        Assertions.assertNotNull(allBids);
        Assertions.assertEquals(4, allBids.size());

        // Add offers
        orderBook.add(offer1);
        orderBook.add(offer2);
        orderBook.add(offer3);
        orderBook.add(offer4);

        List<Order> allOffers = orderBook.getOrdersBySide('O');

        Assertions.assertNotNull(allOffers);
        Assertions.assertEquals(4, allOffers.size());

    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Non existing order side is properly handled.")
    void successfulHandleInvalidOrder() {
        List<Order> allBids = orderBook.getOrdersBySide('B');
        List<Order> allOffers = orderBook.getOrdersBySide('O');

        Order invalidOrder = orderBook.add(new Order(10, 10, 'C', 1));
        Assertions.assertEquals(4, allBids.size());
        Assertions.assertEquals(4, allOffers.size());
        Assertions.assertNull(invalidOrder);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("The orders are added in the correct order.")
    void verifyTheOrderOfTheOrders() {
        // Bids
        List<Order> allBids = orderBook.getOrdersBySide('B');

        // Assert that the highest bid is the bid2 with the highest price
        Assertions.assertEquals(bid2, allBids.get(0));

        // Assert that the bid1 is after bid3 because bid3 has higher size than bid1
        Assertions.assertEquals(bid3, allBids.get(1));
        Assertions.assertEquals(bid1, allBids.get(2));

        // Offers
        List<Order> allOffers = orderBook.getOrdersBySide('O');

        // Assert that the highest bid is the bid2 with the highest price
        Assertions.assertEquals(offer4, allOffers.get(0));
        Assertions.assertEquals(offer3, allOffers.get(1));
        Assertions.assertEquals(offer1, allOffers.get(2));
        Assertions.assertEquals(offer2, allOffers.get(3));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("The orders are successfully modified.")
    void successfullyModifyOrderSize() {
        int newSize = 13;

        // Bids
        orderBook.modifyOrderSize(2, newSize);
        List<Order> bids = orderBook.getOrdersBySide('B');
        Assertions.assertEquals(2, bids.get(0).getId());
        Assertions.assertEquals(newSize, bids.get(0).getSize());

        // Offers
        orderBook.modifyOrderSize(6, newSize);
        List<Order> offers = orderBook.getOrdersBySide('O');
        Assertions.assertEquals(6, offers.get(3).getId());
        Assertions.assertEquals(newSize, offers.get(3).getSize());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("The orders are correctly retrieved by price level.")
    void successfullyRetrieveOffersByPriceLevel() {
        // Bids
        double bidPriceLevel1 = orderBook.getPriceByLevel('B', 1);
        double bidPriceLevel2 = orderBook.getPriceByLevel('B', 2);
        double bidPriceLevel3 = orderBook.getPriceByLevel('B', 3);

        Assertions.assertEquals(bid2.getPrice(), bidPriceLevel1);
        Assertions.assertEquals(bid3.getPrice(), bidPriceLevel2);
        Assertions.assertEquals(bid1.getPrice(), bidPriceLevel2);
        Assertions.assertEquals(bid4.getPrice(), bidPriceLevel3);

        // Offers
        double offerPriceLevel1 = orderBook.getPriceByLevel('O', 1);
        double offerPriceLevel2 = orderBook.getPriceByLevel('O', 2);
        double offerPriceLevel3 = orderBook.getPriceByLevel('O', 3);

        Assertions.assertEquals(offer4.getPrice(), offerPriceLevel1);
        Assertions.assertEquals(offer1.getPrice(), offerPriceLevel2);
        Assertions.assertEquals(offer3.getPrice(), offerPriceLevel2);
        Assertions.assertEquals(offer2.getPrice(), offerPriceLevel3);
    }


    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("Return -1 when price level is not available.")
    void successfullyRetrieveInvalidPriceLevel() {
        double invalidPriceLevel = -1.0;
        // Bids
        double bidPriceLevel4 = orderBook.getPriceByLevel('B', 4);
        Assertions.assertEquals(invalidPriceLevel, bidPriceLevel4);

        // Offers
        double offerPriceLevel7 = orderBook.getPriceByLevel('O', 7);
        Assertions.assertEquals(invalidPriceLevel, offerPriceLevel7);
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("The orders are removed correctly.")
    void successfullyRemoveOrders() {
        // Bids
        orderBook.remove(2);
        List<Order> bids = orderBook.getOrdersBySide('B');

        Assertions.assertEquals(3, bids.size());
        Assertions.assertFalse(bids.contains(bid2));

        // Offer
        orderBook.remove(6);
        List<Order> offers = orderBook.getOrdersBySide('O');

        Assertions.assertEquals(3, offers.size());
        Assertions.assertFalse(offers.contains(offer2));
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @DisplayName("The remove doesn't do anything when no orderId was found.")
    void nothingChangedWhenRemoveNonExistingOrderId() {
        orderBook.remove(9999);
        Assertions.assertEquals(3, orderBook.getOrdersBySide('B').size());
        Assertions.assertEquals(3, orderBook.getOrdersBySide('O').size());
    }
}
