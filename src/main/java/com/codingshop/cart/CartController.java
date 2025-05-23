package com.codingshop.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.codingshop.common.FrontController;
import com.codingshop.members.MembersController;
import com.codingshop.members.MembersDTO;
import com.codingshop.product.ProductController;
import com.codingshop.review.ReviewController;

public class CartController {
	static Scanner sc = new Scanner(System.in);
	CartService cartService = new CartService();
	
	public void execute(MembersDTO member) {
		System.out.println("===장바구니 페이지===");
		// 회원용(매개변수 있음)
		boolean isStop = false;
		while (!isStop) {
			menuDisplay();
			int job = sc.nextInt();
			switch (job) {
				case 1 ->{f_selectAll(member);}
				case 2 ->{f_update(member);}
				case 3 ->{f_clear(member);}
				case 4 ->{f_deleteByNum(member);}
				case 5 ->{f_insert(member);}
				case 6 ->{new MembersController().execute(member);}
				case 7 ->{new ProductController().execute(member);}
				case 8 ->{new ReviewController().execute(member);}
				case 9 ->{member=null;  FrontController.main(null);}
				case 10 ->{return;}
			}
		}
	}
	
	private void f_insert(MembersDTO member) {
		
		ProductController pc = new ProductController();
		pc.f_selectAll(member);
		System.out.print("제품 번호 입력: ");
		int pronum = sc.nextInt();
		System.out.println("수량 입력: ");
		int cnt = sc.nextInt();
		
		int result = cartService.insertCart(member, pronum, cnt);
		
		if(result >0) {
			CartView.display("담기 성공");
		}else {
			CartView.display("담기 실패...");
		}
	}

	private void f_deleteByNum(MembersDTO member) {
		f_selectAll(member);
		System.out.print("삭제할 제품 번호: ");
		int num = sc.nextInt();
		int result = cartService.deleteCartProduct(member,num);
		if(result > 0) {
			CartView.display("장바구니 삭제 성공");
		}else {
			CartView.display("삭제 실패...");
		}
	}

	private void f_clear(MembersDTO member) {
		
		int result = cartService.deleteCartAll(member);
		if(result > 0) {
			CartView.display("장바구니 삭제 성공");
		}else {
			CartView.display("삭제 실패...");
		}
	}

	private void f_update(MembersDTO member) {
		f_selectAll(member);
		System.out.print("수정할 장바구니 번호 입력: ");
		int num = sc.nextInt();
		System.out.println("수량 입력: ");
		int cnt = sc.nextInt();
		CartDTO cart = cartService.select(num, member);
		int result = cartService.updateProductCnt(cart, cnt);
		if(result > 0) {
			CartView.display("update 성공");
		}else {
			CartView.display("update 실패...");
		}
	}

	public void f_selectAll(MembersDTO member) {
		List<CartDTO> clist = new ArrayList<>();
		clist =cartService.selectMyAll(member);
		CartView.display(clist);
	}

	private void menuDisplay() {
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("1. 내 장바구니 조회 | 2. 상품 수 변경 | 3. 내 장바구니 모두 삭제 | 4. 해당 제품 장바구니만 삭제(제품num) | 5.장바구니 담기 \n 6.내 정보 | 7.쇼핑 페이지 |8.리뷰 페이지 | 9.로그아웃 | 10. 되돌아가기");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.print("작업 선택>");
	}
}
