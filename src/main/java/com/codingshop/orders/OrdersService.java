package com.codingshop.orders;

import java.util.List;

import com.codingshop.members.MembersDTO;

public class OrdersService {
	static OrdersDAO ordersDAO = new OrdersDAO();
	
	public int insertAllCart(MembersDTO member) {
		return ordersDAO.insertAllCart(member);
	}
	public int insertOrder(MembersDTO member, int num) {
		return ordersDAO.insertOrder(member, num);
	}
	public int insertOrderby2(MembersDTO member, String name, int cnt) {
		return ordersDAO.insertOrderby2(member, name, cnt);
	}
	public List<OrdersDTO> selectAll(MembersDTO member) {
		return ordersDAO.selectAll(member);
	}
	public OrdersDTO selectByNum(MembersDTO member,int num) {
		return ordersDAO.selectByNum(member,num);
	}
}
