package com.codingshop.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.codingshop.cart.CartController;
import com.codingshop.common.FrontController;
import com.codingshop.members.MembersController;
import com.codingshop.members.MembersDTO;
import com.codingshop.product.ProductController;
import com.codingshop.review.ReviewController;

public class OrdersController {
	static Scanner sc = new Scanner(System.in);
	OrdersService ordersService = new OrdersService();
	
	public void execute(MembersDTO member) {
		System.out.println("===주문 페이지===");
		boolean isStop = false;
		while (!isStop) {
			menuDisplay();
			int job = sc.nextInt();
			switch (job) {
				case 1 ->{f_selectAll(member);}
				case 2 ->{f_selectBynum(member);}
				case 3 ->{f_BuyAll(member);}
				case 4 ->{f_insertOrder(member);}
				case 5 ->{f_insertBy2(member);}
				case 6 ->{new MembersController().execute(member);}
				case 7 ->{new ProductController().execute(member);}
				case 8 ->{new ReviewController().execute(member);}
				case 9 ->{member=null;  FrontController.main(null);}
				case 10 ->{return;}
			}
		}
	}
	
	private void f_insertBy2(MembersDTO member) {
		new ProductController().f_selectAll(member);
		System.out.print("주문할 제품명: ");
		String name = sc.next();
		System.out.print("수량: ");
		int cnt = sc.nextInt();
		int result = ordersService.insertOrderby2(member, name, cnt);
		if(result >0) {
			OrdersView.display("주문 성공!");
		}else {
			OrdersView.display("주문 실패...");
		}
	}

	private void f_insertOrder(MembersDTO member) {
		new CartController().f_selectAll(member);
		System.out.print("주문할 장바구니 번호: ");
		int num = sc.nextInt();
		int result = ordersService.insertOrder(member, num);
		if(result >0) {
			OrdersView.display("주문 성공!");
		}else {
			OrdersView.display("주문 실패...");
		}
	}

	private void f_BuyAll(MembersDTO member) {
		int result = ordersService.insertAllCart(member);
		OrdersView.display(result+"건 주문 완료!");
	}

	private void f_selectBynum(MembersDTO member) {
		System.out.print("조회할 주문 내역 번호>");
		int num = sc.nextInt();
		OrdersDTO order = ordersService.selectByNum(member,num);
		if(order == null) {
			OrdersView.display("번호 확인 필요...");
			return;
		}
		OrdersView.display(order);
	}
	
	private void f_selectAll(MembersDTO member) {
		List<OrdersDTO> olist = new ArrayList<>();
		olist = ordersService.selectAll(member);
		OrdersView.display(olist);
	}

	private void menuDisplay() {
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("1. 내 모든 주문 조회 | 2. 주문 조회(번호) | 3.장바구니 모두 구매 | 4. 장바구니 번호로 주문 | 5. 제품명으로 주문 \n6. 마이페이지 | 7. 상품페이지 | 8. 리뷰페이지 | 9. 로그아웃 | 10. 되돌아가기");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.print("작업 선택>");
	}
}
