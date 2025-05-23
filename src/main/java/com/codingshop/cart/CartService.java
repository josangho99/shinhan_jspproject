package com.codingshop.cart;

import java.util.List;

import com.codingshop.members.MembersDTO;

public class CartService {
	CartDAO cartDAO = new CartDAO();
	
	public int insertCart(MembersDTO member, int pronum,int cnt) {
		return cartDAO.insertCart(member, pronum, cnt);
	}
	
	public List<CartDTO> selectMyAll(MembersDTO member){
		return cartDAO.selectMyAll(member);
	}
	
	public int updateProductCnt(CartDTO cart, int cnt) {
		return cartDAO.updateProductCnt(cart, cnt);
	}
	
	public int deleteCartProduct(MembersDTO member, int num) {
		return cartDAO.deleteCartProduct(member, num);
	}
	
	public int deleteCartAll(MembersDTO member) {
		return cartDAO.deleteCartAll(member);
	}
	public CartDTO select(MembersDTO member, int pronum) {
		return cartDAO.select(member, pronum);
	}
	public CartDTO select(int num , MembersDTO member) {
		return cartDAO.select(num, member);
	}
}
