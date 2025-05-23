package com.codingshop.review;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewDTO {
	private Integer reviewNum;
	private Integer memberNum;
	private Integer productNum;
	private Double rate;
	private String cont; // 리뷰 내용
	private Date rDate;
}
