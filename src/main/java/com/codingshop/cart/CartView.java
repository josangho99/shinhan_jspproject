package com.codingshop.cart;

import java.util.List;

public class CartView {
	public static void display(String message) {
		System.out.println("알림: " + message);
	}
	public static void display(CartDTO cart) {
		System.out.println("장바구니 정보: " + cart);
	}

	public static void display(List<CartDTO> clist) {
		if(clist.size() == 0) {
			display("해당하는 장바구니가 존재하지 않습니다!");
			return;
		}
		System.out.println("=====장바구니 여러건 조회=====");
		clist.stream().forEach(cart -> System.out.println(cart));
	}
}
