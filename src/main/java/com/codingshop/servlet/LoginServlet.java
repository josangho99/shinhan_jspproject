package com.codingshop.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.codingshop.members.MembersDTO;
import com.codingshop.members.MembersService;

@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userId = request.getParameter("user_id");
		String userPw = request.getParameter("pswd");
	
		MembersService mService  = new MembersService();
		MembersDTO member = mService.login(userId, userPw);
		if(member == null) {
			response.sendRedirect("http://localhost:9090/codingshop/");
			return;
		}
		session.setAttribute("user",member);
		response.sendRedirect("shop");
	}

}
