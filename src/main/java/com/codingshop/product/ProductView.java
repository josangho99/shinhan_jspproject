package com.codingshop.product;

import java.util.List;

public class ProductView {
	public static void display(String message) {
		System.out.println("알림: " + message);
	}
	public static void display(ProductDTO product) {
		System.out.println("제품정보: " + product);
	}

	public static void display(List<ProductDTO> prolist) {
		if(prolist.size() == 0) {
			display("해당하는 제품이 존재하지 않습니다!");
			return;
		}
		System.out.println("=====제품 여러건 조회=====");
		prolist.stream().forEach(pro -> System.out.println(pro));
	}
	public static void display(List<String> slist, String target) {
		if(slist.size() == 0) {
			display("해당하는 제품이 존재하지 않습니다!");
			return;
		}
		if(target.equals("cate")) {
			System.out.println("=====카테고리 목록=====");
			slist.stream().forEach(s -> System.out.print(s + "\t"));
		}
		if(target.equals("brand_name")) {
			System.out.println("=====브랜드 목록=====");
			slist.stream().forEach(s -> System.out.print(s +"\t"));
		}
	}
}
