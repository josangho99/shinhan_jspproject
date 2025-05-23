package com.codingshop.review;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.codingshop.common.FrontController;
import com.codingshop.members.MembersController;
import com.codingshop.members.MembersDTO;
import com.codingshop.product.ProductController;

public class ReviewController{
	static Scanner sc = new Scanner(System.in);
	ReviewService reviewService = new ReviewService();
	
	public void execute(MembersDTO nowMember) {
		System.out.println("===리뷰 페이지===");
		boolean isStop = false;
		if (nowMember.getMemeberRole().equalsIgnoreCase("User")) {
			while (!isStop) {
				menuDisplay(nowMember);
				int job = sc.nextInt();
				switch (job) {
					case 1->{f_writeReview(nowMember);}
					case 2->{f_deleteReview(nowMember);}
					case 3->{f_updateReview(nowMember);}
					case 4->{f_selectMyReviews(nowMember);}
					case 5->{f_selectByname();}
					case 6->{new MembersController().execute(nowMember);}
					case 7->{new ProductController().execute(nowMember);}
					case 8->{nowMember=null; FrontController.main(null);}
					case 9->{return;}
				}
			}
		}
		if(nowMember.getMemeberRole().equalsIgnoreCase("Admin")){
			while (!isStop) {
				menuDisplay(nowMember);
				int job = sc.nextInt();
				switch (job) {
				case 1->{f_selectByname();}
				case 2->{f_selectByMemberID();}
				case 3->{f_selectLowRate();}
				case 4->{f_deleteReview(nowMember);}
				case 5->{new MembersController().execute(nowMember);}
				case 6->{new ProductController().execute(nowMember);}
				case 7->{nowMember=null; FrontController.main(null);}
				case 8->{return;}
			}
			}
		}
	}
	private void f_selectLowRate() {
		List<ReviewDTO> rlist = new ArrayList<>();
		rlist = reviewService.selectByRating();
		ReviewView.display(rlist);
	}
	
	private void f_selectByMemberID() {
		List<ReviewDTO> rlist = new ArrayList<>();
		String target = "member_id";
		System.out.print("조회할 회원 ID: ");
		String input = sc.next();
		rlist = reviewService.selectBy2(target, input);
		ReviewView.display(rlist);
	}
	
	private void f_selectMyReviews(MembersDTO member) {
		List<ReviewDTO> rlist = new ArrayList<>();
		rlist = reviewService.selectMyAllReview(member);
		ReviewView.display(rlist);
	}
	
	private void f_updateReview(MembersDTO member) {
		int result = 0;
		f_selectMyReviews(member);
		System.out.print("수정할 리뷰 번호:");
		int num = sc.nextInt();
		ReviewDTO review = reviewService.selectReview(num);
		if(review == null) {
			ReviewView.display("해당 번호 리뷰 없음");
			return;
		}
		System.out.print("평점(0.0~5.0): ");
		Double rate = sc.nextDouble();
		sc.nextLine();
		System.out.print("내용: ");
		String cont = sc.next();
	
		result = reviewService.updateReview(review, cont, rate);
	
		if(result > 0) {
			ReviewView.display("리뷰 변경 성공");
		}else {
			ReviewView.display("리뷰 변경 실패...");
		}
	}
	
	private void f_deleteReview(MembersDTO member) {
		List<ReviewDTO> rlist = new ArrayList<>();
		int result = 0;
		if(member.getMemeberRole().equalsIgnoreCase("User")) {
			rlist = reviewService.selectMyAllReview(member);
		}
		if(member.getMemeberRole().equalsIgnoreCase("Admin")) {
			rlist = reviewService.selectAll();
		}
		ReviewView.display(rlist);
		System.out.print("삭제할 리뷰 번호>");
		int num = sc.nextInt();
		if(rlist.size() != 0) {
			result = reviewService.deleteByID(member,num);
		}
		if(result > 0) {
			ReviewView.display("리뷰 삭제 성공");
		}else {
			ReviewView.display("리뷰 삭제 실패...");
		}
	}
	private void f_writeReview(MembersDTO member) {
		int result = 0;
		System.out.print("제품 번호: ");
		Integer product_num = sc.nextInt();
		System.out.print("평점: ");
		Double rate = sc.nextDouble();
		sc.nextLine();
		System.out.print("내용: ");
		String cont = sc.nextLine();

		ReviewDTO review = ReviewDTO.builder()
				.memberNum(member.getMemberNum())
				.productNum(product_num)
				.rate(rate)
				.cont(cont)
				.build();
		
		result = reviewService.insertReview(review);
		
		if(result >0) {
			ReviewView.display("리뷰 작성 성공!");
		}else {
			ReviewView.display("리뷰 작성 실패");
		}
	}
	private void f_selectByname() {
		List<ReviewDTO> rlist = new ArrayList<>();
		String target = "product_name";
		System.out.print("조회할 상품명: ");
		String input = sc.next();
		rlist = reviewService.selectBy2(target, input);
		ReviewView.display(rlist);
	}
	
	private void menuDisplay(MembersDTO member) {
		if(member.getMemeberRole().equalsIgnoreCase("User")) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1. 리뷰 작성  | 2. 리뷰 삭제 | 3. 리뷰 수정 | 4. 내 리뷰 조회 | 5.상품 리뷰 보기(상품명) \n6. 내 정보 | 7. 상품 페이지 | 8.로그아웃 | 9. 되돌아가기");
			System.out.println("-------------------------------------------------------------------------------");
			System.out.print("작업 선택>");
		}
		if(member.getMemeberRole().equalsIgnoreCase("Admin")) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1. 리뷰 조회(상품)  | 2. 리뷰 조회(회원) | 3. 리뷰 조회(평점 1점 이하) | 4. 리뷰 삭제 \n5. 내 정보 | 6. 상품 페이지 | 7.로그아웃 | 8. 되돌아가기");
			System.out.println("-------------------------------------------------------------------------------");
			System.out.print("작업 선택>");
		}
	}
}
