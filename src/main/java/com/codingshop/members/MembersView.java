package com.codingshop.members;

import java.util.List;

public class MembersView {

	public static void display(String message) {
		System.out.println("알림: " + message);
	}
	public static void display(MembersDTO member) {
		System.out.println("회원정보: " + member);
	}

	public static void display(List<MembersDTO> mlist) {
		if(mlist.size() == 0) {
			display("해당하는 회원이 존재하지 않습니다!");
			return;
		}
		System.out.println("=====회원 여러건 조회=====");
		mlist.stream().forEach(member -> System.out.println(member));
	}
	public static void display(String grade, Integer totalSpent) {
		switch (grade) {
		case "BRONZE"->{
			System.out.printf("당신의 등급은 %s입니다.\n총 사용금액은 %d원이며 다음 등급까지 %d원 남았습니다.\n" ,grade, totalSpent, 50000 - totalSpent);}
		case "SILVER"->{
			System.out.printf("당신의 등급은 %s입니다.\n총 사용금액은 %d원이며 다음 등급까지 %d원 남았습니다.\n" ,grade, totalSpent, 100000 - totalSpent);}
		case "GOLD"->{
			System.out.printf("당신의 등급은 %s입니다.\n총 사용금액은 %d원이며 다음 등급까지 %d원 남았습니다.\n" ,grade, totalSpent, 300000 - totalSpent);}
		case "PLATINUM"->{
			System.out.printf("당신의 등급은 %s입니다.\n총 사용금액은 %d원이며 다음 등급까지 %d원 남았습니다.\n" ,grade, totalSpent, 500000 - totalSpent);
			}
		case "VIP"->{
			System.out.printf("당신의 등급은 %s입니다.\n총 사용금액은 %d원 입니다.\n",grade,totalSpent);
			}
		}
	}
}
