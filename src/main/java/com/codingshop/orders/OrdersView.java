package com.codingshop.orders;

import java.util.List;

public class OrdersView {
	public static void display(String message) {
		System.out.println("알림: " + message);
	}
	public static void display(OrdersDTO order) {
		System.out.println("주문정보: " + order);
	}

	public static void display(List<OrdersDTO> olist) {
		if(olist.size() == 0) {
			display("해당하는 주문이 존재하지 않습니다!");
			return;
		}
		System.out.println("=====주문 여러건 조회=====");
		olist.stream().forEach(order -> System.out.println(order));
	}
}
