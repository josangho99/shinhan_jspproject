package com.codingshop.members;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.codingshop.util.DBUtil;

public class MembersDAO {

	// update(사용금액)
	public int updateTotalSpent(Connection conn, MembersDTO member, int orderPrice) { // Connection 파라미터 추가
	    int result = 0;
	    PreparedStatement st = null;
	    String sql = """
	            update members
	            set total_spent = ?
	            where member_num = ?
	            """;
	    try {
	        st = conn.prepareStatement(sql);
	        if (orderPrice == 0) {
	            return result;
	        }

	        int totalSpent = member.getTotalSpent() + orderPrice;
	        st.setInt(1, totalSpent);
	        st.setInt(2, member.getMemberNum());
	        result = st.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // DBUtil.dbDisconnect(null, st, null);  // 여기서 닫지 않음
	    }

	    return result;
	}

	// update(등급)
	public int updateGrade(Connection conn, MembersDTO member) {  // Connection 파라미터 추가
	    int result = 0;
	    PreparedStatement st = null;
	    String sql = """
	            update members
	            set grade = ?
	            where member_num = ?
	            """;
	    try {
	        st = conn.prepareStatement(sql);
	        String grade = member.getGrade();

	        if (member.getTotalSpent() < 50000) {
	            grade = "BRONZE";
	        } else if (member.getTotalSpent() < 100000) {
	            grade = "SILVER";
	        } else if (member.getTotalSpent() < 300000) {
	            grade = "GOLD";
	        } else if (member.getTotalSpent() < 500000) {
	            grade = "PLATINUM";
	        } else {
	            grade = "VIP";
	        }

	        st.setString(1, grade);
	        st.setInt(2, member.getMemberNum());
	        result = st.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // DBUtil.dbDisconnect(null, st, null);  // 여기서 닫지 않음
	    }

	    return result;
	}

	// 회원 탈퇴
	public int deleteByID(String id) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				delete from members where member_id = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, id);
			result = st.executeUpdate();
		} catch (Exception e) {

		}
		return result;
	}

	// 회원 조회(byID)
	public MembersDTO selectByID(String id) {
		MembersDTO member = null;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select *
				from members
				where member_id = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, id);
			rs = st.executeQuery();
			while (rs.next()) {
				member = makeUser(rs);
			}
		} catch (Exception e) {

		}
		return member;
	}

	// 모든 회원 조회
	public List<MembersDTO> selectAll() {
		List<MembersDTO> mlist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select *
				from members
				""";
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				MembersDTO member = makeUser(rs);
				mlist.add(member);
			}
		} catch (Exception e) {

		}
		return mlist;
	}

	// 회원가입
	public int signUp(MembersDTO member) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				insert into members(
				member_num,
				member_id,
				member_password,
				memberofname,
				member_role,
				total_spent,
				grade,
				call_number,
				address,
				detail_address
				) values(MEMBER_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, member.getMemberID());
			st.setString(2, member.getMemberPassword());
			st.setString(3, member.getMemberName());
			st.setString(4, member.getMemeberRole());
			st.setInt(5, member.getTotalSpent());
			st.setString(6, member.getGrade());
			st.setString(7, member.getCallNumber());
			st.setString(8, member.getAddress());
			st.setString(9, member.getDetailAddress());
			result = st.executeUpdate();
		} catch (Exception e) {

		}
		return result;
	}

	// 로그아웃
	public MembersDTO logout() {
		return null;
	}

	// 로그인
	public MembersDTO login(String id, String password) {
		MembersDTO user = null;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = """
				select * from members
				where member_id= ? and member_password= ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, id);
			st.setString(2, password);
			rs = st.executeQuery();
			while (rs.next()) {
				user = makeUser(rs);
			}
		} catch (SQLException e) {
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return user;
	}

	private MembersDTO makeUser(ResultSet rs) throws SQLException {
		MembersDTO user = MembersDTO.builder().memberID(rs.getString("member_id"))
				.memberPassword(rs.getString("member_password")).memberName(rs.getString("memberofname"))
				.memberNum(rs.getInt("member_num")).address(rs.getString("address"))
				.callNumber(rs.getString("call_number")).memeberRole(rs.getString("member_role"))
				.grade(rs.getString("grade")).totalSpent(rs.getInt("total_spent"))
				.detailAddress(rs.getString("detail_address")).build();
		return user;
	}
}
