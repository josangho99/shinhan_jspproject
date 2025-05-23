package com.codingshop.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.codingshop.members.MembersDTO;
import com.codingshop.util.DBUtil;

public class ProductDAO {

	// 상품 조회(번호) where
		public ProductDTO selectByNum(int num) {
			ProductDTO product = null;
			Connection conn = DBUtil.getConnection();
			PreparedStatement st = null;
			ResultSet rs = null;

			String sql = """
					select *
					from product
					where product_num = ?
					"""; 
			try {
				st = conn.prepareStatement(sql);
				st.setInt(1, num);
				rs = st.executeQuery();
				while (rs.next()) {
					product = makeProduct(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return product;
		}
	
	//카테고리 조회
	//브랜드 조회
	public List<String> selectList(String target) {
		List<String> selectList = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		Statement  st = null;
		ResultSet rs = null;

		 String sql = "SELECT DISTINCT " + target + " FROM product WHERE is_active = 1";
		 
		try {
			 st = conn.createStatement();
		        rs = st.executeQuery(sql);
		        while (rs.next()) {
		            selectList.add(rs.getString(1));
		        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return selectList;
	}
	
	// 상품 조회(카테고리) where
	// 상품 조회(브랜드) where
	public List<ProductDTO> selectBy2(String target, String input) {
		List<ProductDTO> prolist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = "select * from product where is_active = 1 and +" + target + " = ?"; 
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, input);
			rs = st.executeQuery();
			while (rs.next()) {
				ProductDTO product = makeProduct(rs);
				prolist.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prolist;
	}
	
	// 상품 조회(상품명) where
	public ProductDTO selectByName(String name) {
		ProductDTO product = null;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = """
				select *
				from product
				where product_name = ?
				"""; 
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, name);
			rs = st.executeQuery();
			while (rs.next()) {
				product = makeProduct(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}
	// 상품 조회(키워드) where like
	public List<ProductDTO> selectByKeyword(String input) {
		List<ProductDTO> prolist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = "select * from product where is_active = 1 and product_name like '%"+ input +"%' "; 
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ProductDTO product = makeProduct(rs);
				prolist.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prolist;
	}

	// 모든 상품 조회
	public List<ProductDTO> selectAll(MembersDTO member) {
		List<ProductDTO> prolist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = "";
		if(member == null) {
			sql = """
					select *
					from product
					where is_active = 1
					""";
		}else {
		
			if (member.getMemeberRole().equalsIgnoreCase("Admin")) {
				sql = """
						select *
						from product
						""";
			} else {
				sql = """
						select *
						from product
						where is_active = 1
						""";
			}
		}
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ProductDTO product = makeProduct(rs);
				prolist.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prolist;
	}
	
	// 최신순 상품 조회 desc
	// 매출순 상품 조회 desc
	// 평점순 상품 조회 desc
	public List<ProductDTO> selectAll(String orderMenu) {
		List<ProductDTO> prolist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		Statement  st = null;
		ResultSet rs = null;

		String sql = "select * from product where is_active = 1 order by "+orderMenu+" desc";
		
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery(sql);
			
			while (rs.next()) {
				ProductDTO product = makeProduct(rs);
				prolist.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prolist;
	}

	// 상품 재고 관리(추가) update
	public int updateInventory(ProductDTO product, int num) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		num = product.getInventory() + num;
		String sql = """
				update product 
				set inventory = ?
				where product_name = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, num);
			st.setString(2, product.getProductName());
			result = st.executeUpdate();
			if(num == 0) {
				deleteByName(product.getProductName());
			}else {
				updateActive(product.getProductName());
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	// 상품 판매량 update
		public int updateSales(ProductDTO product, int num) {
			int result = 0;
			Connection conn = DBUtil.getConnection();
			PreparedStatement st = null;
			num = product.getTotalSales() + num;
			String sql = """
					update product 
					set total_sales = ?
					where product_name = ?
					""";
			try {
				st = conn.prepareStatement(sql);
				st.setInt(1, num);
				st.setString(2, product.getProductName());
				result = st.executeUpdate();
				if(num == 0) {
					deleteByName(product.getProductName());
				}else {
					updateActive(product.getProductName());
				}
			} catch (Exception e) {
				
			}
			return result;
		}
	// 상품 등록 insert
	public int insertProduct(ProductDTO product) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				insert into product(
					product_num,
					product_name,
					price,
					created_at,
					inventory,
					total_sales,
					cate,
					is_active,
					brand_name
					avg_rating
				)values(PRODUCT_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, product.getProductName());
			st.setInt(2, product.getPrice());
			st.setDate(3, product.getCreatedAt());
			st.setInt(4, product.getInventory());
			st.setInt(5, product.getTotalSales());
			st.setString(6, product.getCate());
			st.setInt(7, product.getIsActive());
			st.setString(8, product.getBrandName());
			st.setDouble(9, product.getAvgRating());
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	// 상품 삭제(is_active = 0 update)
	public int deleteByName(String name) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				update product 
				set is_active = 0
				where product_name = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, name);
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	// 상품 활성화(is_active = 1 update)
		public int updateActive(String name) {
			int result = 0;
			Connection conn = DBUtil.getConnection();
			PreparedStatement st = null;
			String sql = """
					update product 
					set is_active = 1
					where product_name = ?
					""";
			try {
				st = conn.prepareStatement(sql);
				st.setString(1, name);
				result = st.executeUpdate();
			} catch (Exception e) {
				
			}
			return result;
		}
	
	private ProductDTO makeProduct(ResultSet rs) throws SQLException {
		ProductDTO product = ProductDTO.builder().productNum(rs.getInt("product_num"))
				.productName(rs.getString("product_name")).brandName(rs.getString("brand_name"))
				.cate(rs.getString("cate")).createdAt(rs.getDate("created_at")).price(rs.getInt("price"))
				.inventory(rs.getInt("inventory")).totalSales(rs.getInt("total_sales")).isActive(rs.getInt("is_active"))
				.avgRating(rs.getDouble("avg_rating"))
				.build();
		return product;
	}

}
