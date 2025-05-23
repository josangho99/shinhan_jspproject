package com.codingshop.orders;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrdersDTO {
	private Integer orderNum;
	private Integer memeberNum;
	private Date orderDate;
	private Integer productNum;
	private Integer cnt;
	private Integer orderPrice;
	private String address;
	private String detailAddress;
}
