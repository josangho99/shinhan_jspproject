package com.codingshop.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.codingshop.members.MembersDTO;
import com.codingshop.util.DBUtil;

public class ReviewDAO {
	//리뷰작성 - insert
	public int insertReview(ReviewDTO review) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				insert into review(
					review_num,
					product_num,
					member_num,
					RDATE,
					RATE,
					CONT
				)values(review_seq.NEXTVAL, ?, ?, sysdate, ? ,?)
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, review.getProductNum());
			st.setInt(2, review.getMemberNum());
			st.setDouble(3, review.getRate());
			st.setString(4, review.getCont());
			
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public ReviewDTO selectReview(int num){
		ReviewDTO review = null;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from review where review_num = ?";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, num);
			rs = st.executeQuery();
			while (rs.next()) {
				review = makeReview(rs);
			}
		} catch (SQLException e) {
		
		}
		return review;
	}
	
	
	//내 리뷰 보기 - list where
	public List<ReviewDTO> selectMyAllReview(MembersDTO member){
		List<ReviewDTO> rlist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "select * from review where member_num = ?";
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, member.getMemberNum());
			rs = st.executeQuery();
			while (rs.next()) {
				ReviewDTO review = makeReview(rs);
				rlist.add(review);
			}
		} catch (SQLException e) {
		
		}
		
		return rlist;
	}
	public List<ReviewDTO> selectAll(){
		List<ReviewDTO> rlist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql = "select * from review";
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ReviewDTO review = makeReview(rs);
				rlist.add(review);
			}
		} catch (SQLException e) {
		
		}
		
		return rlist;
	}
	
	//리뷰 삭제 delete
	public int deleteByID(MembersDTO member, int num) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = "";
		if(member.getMemeberRole().equalsIgnoreCase("User")) {
			sql = "delete from review where member_num = "+member.getMemberNum() +" and review_num = ?";
		}
		if(member.getMemeberRole().equalsIgnoreCase("Admin")) {
			sql = """
					delete from review where review_num = ?
					""";
			}
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, num);
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	//리뷰 수정 update
	//리뷰 수정할 번호 선택 -> 평점(변경 안할 시 원래점수) -> 내용 입력
	public int updateReview(ReviewDTO review, String cont, double rate) {
		int result = 0;
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				update review 
				set cont = ?, rdate = sysdate, rate = ?
				where review_num = ?
				""";
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, cont);
			st.setDouble(2, rate);
			st.setInt(3, review.getReviewNum());
			result = st.executeUpdate();
		} catch (Exception e) {
			
		}
		return result;
	}
	
	
	//상품 리뷰 보기(상품명) where list
	//리뷰 조회(회원) list ,where
	public List<ReviewDTO> selectBy2(String target, String input){
		List<ReviewDTO> rlist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "";
		if(target.equals("product_name")) {
			sql = "select * from review "
					+ "where product_num = "
					+ "(select product_num "
					+ "from product where " + target
					+ "= ?)";
		}
		
		if(target.equals("member_id")) {
			sql = "select * from review "
					+ "where member_num = "
					+ "(select member_num "
					+ "from members where " + target
					+ "= ?)";
		}
		
		if(sql.equals("")) {
			return rlist;
		}
		
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, input);
			rs = st.executeQuery();
			while (rs.next()) {
				ReviewDTO review = makeReview(rs);
				rlist.add(review);
			}
		} catch (SQLException e) {
		
		}
		
		return rlist;
	}
	//리뷰 조회(평점 1점 이하) list, where
	public List<ReviewDTO> selectByRating(){
		List<ReviewDTO> rlist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		String sql = "select * from review where rate <= 1";
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ReviewDTO review = makeReview(rs);
				rlist.add(review);
			}
		} catch (SQLException e) {
		
		}
		
		return rlist;
	}
	private ReviewDTO makeReview(ResultSet rs) throws SQLException{
		ReviewDTO review = ReviewDTO.builder()
				.reviewNum(rs.getInt("review_num"))
				.memberNum(rs.getInt("member_num"))
				.productNum(rs.getInt("product_num"))
				.rate(rs.getDouble("rate"))
				.cont(rs.getString("cont"))
				.rDate(rs.getDate("rdate"))
				.build();
		return review;
	}
}
