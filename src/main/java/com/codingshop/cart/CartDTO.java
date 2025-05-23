package com.codingshop.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartDTO {
	private Integer cartNum;
	private Integer memberNum;
	private Integer productNum;
	private Integer cnt; // 제품 수량
}
