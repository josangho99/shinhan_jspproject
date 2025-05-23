package com.codingshop.members;

import java.util.Scanner;

import com.codingshop.common.CommonControllerInterface;

public class MembersSignUpController implements CommonControllerInterface{
	static Scanner sc = new Scanner(System.in);
	static MembersService membersService = new MembersService();
	
	@Override
	public void execute() {
		MembersDTO member = new MembersDTO();
		member = makeMember();
		int result = membersService.signUp(member);
		if(result == 0) {
			MembersView.display("회원가입 실패");
			return;
		}
		else {
			MembersView.display("회원가입 성공!");
		}
	}
	
	static MembersDTO makeMember() {
		System.out.print("ID: ");
		
		String member_id = sc.next();
		System.out.println(membersService.selectByID(member_id));
		
		if(new MembersDAO().selectByID(member_id) != null)	{
			System.out.print("중복된 아이디 입니다...\n");
			return null;
		}
		
		System.out.print("Password: ");
		String member_password = sc.next();
		System.out.print("Name: ");
		String memberofname = sc.next();
		System.out.print("도로명 주소: ");
		String address = sc.next();
		System.out.print("상세 주소: ");
		String detail_address = sc.next();
		System.out.print("전화번호(-생략): ");
		String call_number = sc.next();
		
		MembersDTO user = MembersDTO.builder()
				.memberID(member_id)
				.memberPassword(member_password)
				.memberName(memberofname)
				.address(address)
				.callNumber(call_number)
				.memeberRole("User")
				.grade("BRONZE")
				.totalSpent(0)
				.detailAddress(detail_address)
				.build();
		return user;
	}
	
}
