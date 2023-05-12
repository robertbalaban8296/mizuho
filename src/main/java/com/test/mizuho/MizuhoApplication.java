package com.test.mizuho;

import com.test.mizuho.dao.Order;
import com.test.mizuho.service.OrderBook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class MizuhoApplication {

	public static void main(String[] args) {
		OrderBook orderBook = new OrderBook();

		orderBook.add(new Order(1, 20.0, 'B', 32));
		orderBook.add(new Order(2, 30.0, 'O', 4));
		orderBook.add(new Order(3, 41.0, 'B', 5));
		orderBook.add(new Order(4, 15.7, 'O', 3));
		orderBook.add(new Order(5, 10, 'B', 1));
		orderBook.add(new Order(6, 20.0, 'O', 55));

		orderBook.remove(1);
		orderBook.modifyOrderSize(2, 30);
		orderBook.getOrdersBySide('B');
		orderBook.getPriceByLevel('B', 2);
		orderBook.getTotalSizeByLevel('O', 1);
	}

}
