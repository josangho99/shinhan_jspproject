package com.codingshop.members;

import java.util.List;

public class MembersService {
	MembersDAO userDAO = new MembersDAO();
	
	public int deleteByID(String id) {
		return userDAO.deleteByID(id);
	}
	
	public MembersDTO selectByID(String id) {
		return userDAO.selectByID(id);
	}
	
	public List<MembersDTO> selectAll(){
		return userDAO.selectAll();
	}
	
	public int signUp(MembersDTO member) {
		return userDAO.signUp(member);
	}
	
	public int deleteMe(MembersDTO member) {
		return userDAO.deleteByID(member.getMemberID());
	}
	
	public MembersDTO logout() {
		return userDAO.logout();
	}
	
	public MembersDTO login(String id, String password) {
		return userDAO.login(id,password);
	}
}
