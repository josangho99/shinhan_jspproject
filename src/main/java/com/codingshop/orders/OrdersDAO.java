package com.codingshop.orders;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.codingshop.cart.CartDAO;
import com.codingshop.cart.CartDTO;
import com.codingshop.members.MembersDAO;
import com.codingshop.members.MembersDTO;
import com.codingshop.product.ProductDAO;
import com.codingshop.product.ProductDTO;
import com.codingshop.util.DBUtil;

public class OrdersDAO {
	static ProductDAO productDAO = new ProductDAO();
	static MembersDAO membersDAO = new MembersDAO();
	static CartDAO cartDAO = new CartDAO();

	// 모든 장바구니 주문
	public int insertAllCart(MembersDTO member) {
		int result = 0;
		List<CartDTO> clist = new ArrayList<>();
		clist = new CartDAO().selectMyAll(member);
		if (clist == null) {
			return result;
		}

		for (CartDTO cart : clist) {
			result += insertOrder(member, cart.getCartNum());
		}

		return result;
	}

	// 해당(번호) 장바구니 주문
	public int insertOrder(MembersDTO member, int num) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;

		// cart 조회하여 반환
		CartDTO cart = new CartDAO().select(num, member);
		if (cart == null) {
			return result;
		}

		String sql = """
				insert into orders(
					order_num,
					member_num,
					order_date,
					product_num,
					cnt,
					order_price,
					address,
					detail_address
				)values(orders_seq.nextval,?,sysdate,?,?,?,?,?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			st.setInt(2, cart.getProductNum());
			st.setInt(3, cart.getCnt());
			int order_price = 0;
			ProductDTO product = productDAO.selectByNum(cart.getProductNum());
			if (product == null) {
				return result;
			}
			if (product.getInventory() < cart.getCnt()) {
				return result;
			}
			// 총 금액
			order_price = product.getPrice() * cart.getCnt();
			st.setInt(4, order_price);
			st.setString(5, member.getAddress());
			st.setString(6, member.getDetailAddress());
			result = st.executeUpdate();
			conn.setAutoCommit(false); // 트랜잭션 시작
			// 제품 수량 변경
			if (result > 0) {
				int resultProduct = productDAO.updateInventory(product, -1 * cart.getCnt());
				productDAO.updateSales(product, cart.getCnt());

				// 최신 멤버 정보 조회
				MembersDTO latestMember = membersDAO.selectByID(member.getMemberID());
				if (latestMember != null) {
					membersDAO.updateTotalSpent(conn, latestMember, order_price);
					membersDAO.updateGrade(conn, latestMember);
				} else {
					throw new SQLException("Member not found"); // 예외 발생
				}

				cartDAO.deleteCartNum(cart);
				if (resultProduct < 0) {
					throw new SQLException("Inventory update failed"); // 예외 발생
				}
			}
			conn.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			try {
				conn.rollback(); // 롤백
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null); // 연결 종료
		}
		return result;
	}

	// 제품명 및 수량으로 주문
	public int insertOrderby2(MembersDTO member, String name, int cnt) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;

		// 제품 이름 검색으로 반환
		ProductDTO product = productDAO.selectByName(name);
		if (product == null) {
			return result;
		}
		// 수량보다 적으면 반환하여 끝냄
		if (product.getInventory() < cnt) {
			return result;
		}
		String sql = """
				insert into orders(
					order_num,
					member_num,
					order_date,
					product_num,
					cnt,
					order_price,
					address,
					detail_address
				)values(orders_seq.nextval,?,sysdate,?,?,?,?,?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			st.setInt(2, product.getProductNum());
			st.setInt(3, cnt);
			int order_price = 0;
			// 총 금액
			order_price = product.getPrice() * cnt;
			st.setInt(4, order_price);
			st.setString(5, member.getAddress());
			st.setString(6, member.getDetailAddress());
			result = st.executeUpdate();
			conn.setAutoCommit(false); // 트랜잭션 시작
			// 제품 수량 변경, member total_spent grade update
			if (result > 0) {
				productDAO.updateInventory(product, -1 * cnt);
				productDAO.updateSales(product, cnt);

				// 최신 멤버 정보 조회
				MembersDTO latestMember = membersDAO.selectByID(member.getMemberID());
				if (latestMember != null) {
					membersDAO.updateTotalSpent(conn, latestMember, order_price);
					membersDAO.updateGrade(conn, latestMember);
				} else {
					throw new SQLException("Member not found"); // 예외 발생
				}

			}
			conn.commit(); // 트랜잭션 커밋

		} catch (Exception e) {
			try {
				conn.rollback(); // 롤백
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null); // 연결 종료
		}
		return result;
	}

	// 내 주문 내역 확인
	public List<OrdersDTO> selectAll(MembersDTO member) {
		List<OrdersDTO> olist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = "select * from orders where member_num = ?";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			rs = st.executeQuery();
			while (rs.next()) {
				OrdersDTO order = makeOrder(rs);
				olist.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return olist;
	}

	// 상품 주문 내역 확인
	public List<OrdersDTO> selectByPro(String name) {
		List<OrdersDTO> olist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = """
				select * from orders
				where product_num =
				(select product_num
				from product
				where product_name = ?
				)
				""";

		try {
			st = conn.prepareStatement(sql);
			st.setString(1, name);
			rs = st.executeQuery();

			while (rs.next()) {
				OrdersDTO order = makeOrder(rs);
				olist.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return olist;
	}

	// 모든 주문 내역 확인
	public List<OrdersDTO> selectAll() {
		List<OrdersDTO> olist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;

		String sql = "select * from orders";

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				OrdersDTO order = makeOrder(rs);
				olist.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return olist;
	}

	// 주문 내역(번호) 1개 조회
	public OrdersDTO selectByNum(MembersDTO member, int num) {
		OrdersDTO order = null;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		String sql = """
				select *
				from orders
				where order_num = ?
				and member_num = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, num);
			st.setInt(2, member.getMemberNum());
			rs = st.executeQuery();
			while (rs.next()) {
				order = makeOrder(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	private OrdersDTO makeOrder(ResultSet rs) throws SQLException {
		OrdersDTO order = OrdersDTO.builder().orderNum(rs.getInt("order_num")).memeberNum(rs.getInt("member_num"))
				.orderDate(rs.getDate("order_date")).productNum(rs.getInt("product_num")).cnt(rs.getInt("cnt"))
				.orderPrice(rs.getInt("order_price")).address(rs.getString("address"))
				.detailAddress(rs.getString("detail_address")).build();
		return order;
	}
}