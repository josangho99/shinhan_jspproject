package com.codingshop.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.codingshop.members.MembersDTO;
import com.codingshop.product.ProductDTO;
import com.codingshop.product.ProductService;


@WebServlet("/shop")
public class ShopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    // doGet: 페이지를 처음 로드할 때 또는 정렬/검색 파라미터 없이 접근할 때 사용
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 요청 파라미터 인코딩 설정 (GET 요청에도 적용)

		ProductService pService = new ProductService();
		HttpSession session = request.getSession();
		MembersDTO loggedInMember = (MembersDTO)session.getAttribute("user"); // "user" 세션 속성명 확인

		List<ProductDTO> plist = pService.selectAll(loggedInMember); // 기본 모든 상품 조회
		request.setAttribute("products",plist);
		request.getRequestDispatcher("jsp/product.jsp").forward(request, response);
	}

    // doPost: 정렬 기준이 선택되어 POST 요청이 들어올 때 사용
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 요청 파라미터 인코딩 설정 (POST 요청에 적용)

		ProductService pService = new ProductService();
		List<ProductDTO> products = null;

		// POST 요청으로 넘어온 'order' 파라미터 처리
		String orderParam = request.getParameter("order");

		if (orderParam != null && !orderParam.isEmpty()) {
			products = pService.selectAll(orderParam); // orderMenu 파라미터 사용
		} else {
            // 'order' 파라미터가 없으면 (예외 상황 또는 다른 POST 요청) 기본 조회
            HttpSession session = request.getSession();
            MembersDTO loggedInMember = (MembersDTO) session.getAttribute("user");
            products = pService.selectAll(loggedInMember);
		}

		// 조회된 상품 목록을 request scope에 저장
		request.setAttribute("products", products);

		// shop.jsp 페이지로 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/product.jsp"); // JSP 파일 경로 확인
		dispatcher.forward(request, response);
	}
}