package com.codingshop.members;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MembersDTO {
	private Integer memberNum;
	private String memberID;
	private String memberPassword;
	private String memberName;
	private String memeberRole;
	private Integer totalSpent;
	private String grade;
	private String callNumber;
	private String address;
	private String detailAddress;
}
