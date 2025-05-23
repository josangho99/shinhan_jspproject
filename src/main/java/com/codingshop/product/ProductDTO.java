package com.codingshop.product;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDTO {
	private Integer productNum;
	private String productName;
	private Integer price;
	private Date createdAt;
	private Integer inventory;
	private Integer totalSales;
	private String cate; // 카테고리
	private Integer isActive; // 품절 or 단종일 때 0
	private String brandName;
	private Double avgRating;
}
