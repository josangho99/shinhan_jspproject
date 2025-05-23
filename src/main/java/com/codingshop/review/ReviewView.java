package com.codingshop.review;

import java.util.List;

public class ReviewView {
	public static void display(String message) {
		System.out.println("알림: " + message);
	}
	public static void display(ReviewDTO review) {
		System.out.println("리뷰정보: " + review);
	}

	public static void display(List<ReviewDTO> rlist) {
		if(rlist.size() == 0) {
			display("해당하는 리뷰가 존재하지 않습니다!");
			return;
		}
		System.out.println("=====리뷰 여러건 조회=====");
		rlist.stream().forEach(r -> System.out.println(r));
	}
}
