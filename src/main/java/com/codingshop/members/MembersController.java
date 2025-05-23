package com.codingshop.members;

import java.util.List;
import java.util.Scanner;

import com.codingshop.cart.CartController;
import com.codingshop.common.CommonControllerInterface;
import com.codingshop.common.FrontController;
import com.codingshop.orders.OrdersController;
import com.codingshop.product.ProductController;
import com.codingshop.review.ReviewController;

public class MembersController implements CommonControllerInterface {
	static Scanner sc = new Scanner(System.in);
	static MembersService membersService = new MembersService();

	@Override
	public void execute() {
		// 로그인
		boolean isStop = false;
		MembersDTO nowMember = login();

		if (nowMember == null) {
			MembersView.display("로그인 실패!");
			FrontController.main(null);
		}
		System.out.println("===내 정보 메뉴===");
		if (nowMember.getMemeberRole().equalsIgnoreCase("User")) {
			while (!isStop) {
				menuDisplay(nowMember.getMemeberRole());
				int job = sc.nextInt();
				switch (job) {
					case 1->{new ProductController().execute(nowMember);}
					//장바구니
					case 2->{new CartController().execute(nowMember);;}
					case 3->{new ReviewController().execute(nowMember);}
					//주문내역
					case 4->{new OrdersController().execute(nowMember);}
					case 5->{f_checkGrade(nowMember);}
					case 6->{f_deleteMember(nowMember); return;}
					case 7->{nowMember = membersService.logout(); return;}
				}
			}
		}else {
			while (!isStop) {
				menuDisplay(nowMember.getMemeberRole());
				int job = sc.nextInt();
				switch (job) {
				case 1->{new ProductController().execute(nowMember);}
				case 2->{new ReviewController().execute(nowMember);}
				case 3->{f_showAllUser();}
				case 4->{f_selectUser();}
				case 5->{f_deleteUser();}
				case 6->{nowMember = membersService.logout(); return;}
			}
			}
		}
	}
	
	public void execute(MembersDTO nowMember) {
		// 로그인
		boolean isStop = false;
		System.out.println("===내 정보 메뉴===");
		if (nowMember.getMemeberRole().equalsIgnoreCase("User")) {
			while (!isStop) {
				menuDisplay(nowMember.getMemeberRole());
				int job = sc.nextInt();
				switch (job) {
					case 1->{new ProductController().execute(nowMember);}
					//장바구니
					case 2->{new CartController().execute(nowMember);;}
					//리뷰
					case 3->{new ReviewController().execute(nowMember);}
					//주문내역
					case 4->{new OrdersController().execute(nowMember);}
					case 5->{f_checkGrade(nowMember);}
					case 6->{f_deleteMember(nowMember); return;}
					case 7->{nowMember = membersService.logout(); FrontController.main(null);}
				}
			}
		}else {
			while (!isStop) {
				menuDisplay(nowMember.getMemeberRole());
				int job = sc.nextInt();
				switch (job) {
				case 1->{new ProductController().execute(nowMember);}
				//리뷰관리
				case 2->{new ReviewController().execute(nowMember);}
				case 3->{f_showAllUser();}
				case 4->{f_selectUser();}
				case 5->{f_deleteUser();}
				case 6->{nowMember = membersService.logout(); FrontController.main(null);}
			}
			}
		}
	}

	private void f_checkGrade(MembersDTO member) {
	    // 최신 회원 정보 조회
	    MembersDTO latestMember = membersService.selectByID(member.getMemberID());
	    if (latestMember != null) {
	        String grade = latestMember.getGrade();
	        Integer totalSpent = latestMember.getTotalSpent();
	        MembersView.display(grade, totalSpent);
	    } else {
	        MembersView.display("회원 정보를 찾을 수 없습니다.");
	    }
	}

	private void f_showAllUser() {
		List<MembersDTO> mlist= membersService.selectAll();
		MembersView.display(mlist);
	}

	private void f_selectUser() {
		System.out.print("조회할 userID: ");
		String id = sc.next();
		MembersDTO member = membersService.selectByID(id);
		MembersView.display(member);
	}

	private void f_deleteMember(MembersDTO member) {
		int result = membersService.deleteMe(member);
		if(result > 0) {
			System.out.println("회원 탈퇴 완료!");
		}else{
			System.out.println("회원 탈퇴 실패!");
		}
	}
	
	private void f_deleteUser() {
		System.out.print("삭제할 회원의 ID:");
		String id = sc.next();
		int result = 0;
		if(membersService.selectByID(id) == null) {
			return;
		}
		if(membersService.selectByID(id).getMemeberRole().equalsIgnoreCase("User"))
		{
			result = membersService.deleteByID(id);
		}
		
		if(result > 0) {
			System.out.println("회원 탈퇴 완료!");
		}else{
			System.out.println("회원 탈퇴 실패!");
			System.out.println("ID 혹은 직책 확인 필요");
		}
	}

	private void menuDisplay(String memeberRole) {
		if(memeberRole.equalsIgnoreCase("User")) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1. 쇼핑 | 2. 장바구니 | 3. 리뷰 | 4. 주문내역 확인\n 5.등급 확인 | 6. 회원 탈퇴 7. 로그아웃");
			System.out.println("-------------------------------------------------------------------------------");
			System.out.print("작업 선택>");
		}else {
			System.out.println("----------------------------------------------------------------------------");
			System.out.println("1. 상품관리 | 2. 리뷰관리 | 3. 모든 회원 조회 | 4. 회원 조회(ID)\n 5. 회원 탈퇴 시키기 6. 로그아웃");
			System.out.println("----------------------------------------------------------------------------");
			System.out.print("작업 선택>");
		}
	}

	private MembersDTO login() {
		System.out.print("ID: ");
		String id = sc.next();
		System.out.print("PW: ");
		String pw = sc.next();
		return membersService.login(id, pw);
	}
}
