package com.codingshop.review;

import java.util.List;

import com.codingshop.members.MembersDTO;

public class ReviewService {
	ReviewDAO reviewDAO = new ReviewDAO();
	
	public int insertReview(ReviewDTO review) {
		return reviewDAO.insertReview(review);
	}
	
	public List<ReviewDTO> selectMyAllReview(MembersDTO member){
		return reviewDAO.selectMyAllReview(member);
	}
	
	public int deleteByID(MembersDTO member, int num) {
		return reviewDAO.deleteByID(member, num);
	}
	
	public int updateReview(ReviewDTO review, String cont, double rate) {
		return reviewDAO.updateReview(review, cont, rate);
	}
	
	public List<ReviewDTO> selectBy2(String target, String input){
		return reviewDAO.selectBy2(target, input);
	}
	
	public List<ReviewDTO> selectByRating(){
		return reviewDAO.selectByRating();
	}
	public List<ReviewDTO> selectAll(){
		return reviewDAO.selectAll();
	}
	public ReviewDTO selectReview(int num) {
		return reviewDAO.selectReview(num);
	}
}
