package com.codingshop.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.codingshop.members.MembersDTO;
import com.codingshop.util.DBUtil;

public class CartDAO {
	
	//장바구니 담기
	public int insertCart(MembersDTO member, int pronum,int cnt) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		CartDTO cart = select(member,pronum);
		//담을 때 동일 제품 + 동일 인물이면 수량만 추가
		if(cart != null) {
			return updateProductCnt(cart, cnt);
		}
		String sql = """
				insert into cart(
					cart_num,
					member_num,
					product_num,
					cnt
				)values(cart_seq.nextval,?,?,?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			st.setInt(2, pronum);
			st.setInt(3, cnt);
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	//장바구니 보기
	public List<CartDTO> selectMyAll(MembersDTO member) {
		List<CartDTO> cartlist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select * 
				from cart
				where MEMBER_NUM = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			rs = st.executeQuery();
			while (rs.next()) {
				CartDTO cart = makeCart(rs);
				cartlist.add(cart);
			}
		} catch (Exception e) {
			
		}
		return cartlist;
	}
	
	public CartDTO select(int num , MembersDTO member) {
		CartDTO cart = null;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = """
				select *
				from cart
				where cart_num = ? and member_num = ?
				"""; 
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, num);
			st.setInt(2, member.getMemberNum());
			rs = st.executeQuery();
			while (rs.next()) {
				cart = makeCart(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cart;
	}
	
	//조회
	public CartDTO select(MembersDTO member, int pronum) {
		CartDTO cart = null;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = """
				select *
				from cart
				where member_num = ? and product_num = ?
				"""; 
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			st.setInt(2, pronum);
			rs = st.executeQuery();
			while (rs.next()) {
				cart = makeCart(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cart;
	}
	
	//수량 변경
	public int updateProductCnt(CartDTO cart, int cnt) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		if(cart ==null) {
			return 0;
		}
		
		cnt = cart.getCnt() + cnt;
		
		if(cnt < 1) {
			return deleteCartNum(cart);
		}
		
		String sql = """
				update cart 
				set cnt = ?
				where cart_num = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, cnt);
			st.setInt(2, cart.getCartNum());
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	//번호로 삭제
	public int deleteCartNum(CartDTO cart) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				delete from cart where cart_num = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, cart.getCartNum());
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	
	//그 제품만 뺴기
	public int deleteCartProduct(MembersDTO member, int num) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				delete from cart where member_num = ? and product_num = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			st.setInt(2, num);
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	//장바구니 뺴기
	public int deleteCartAll(MembersDTO member) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				delete from cart where member_num = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	private CartDTO makeCart(ResultSet rs) throws SQLException {
		CartDTO cart = CartDTO.builder()
				.cartNum(rs.getInt("cart_num"))
				.cnt(rs.getInt("cnt"))
				.memberNum(rs.getInt("member_num"))
				.productNum(rs.getInt("product_num"))
				.build();
		return cart;
	}

}
