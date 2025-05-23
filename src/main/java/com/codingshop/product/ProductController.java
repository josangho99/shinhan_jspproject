package com.codingshop.product;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.codingshop.cart.CartController;
import com.codingshop.common.CommonControllerInterface;
import com.codingshop.common.FrontController;
import com.codingshop.members.MembersController;
import com.codingshop.members.MembersDTO;
import com.codingshop.orders.OrdersController;
import com.codingshop.orders.OrdersDAO;
import com.codingshop.orders.OrdersDTO;
import com.codingshop.orders.OrdersView;
import com.codingshop.review.ReviewController;
import com.codingshop.util.DateUtil;

public class ProductController implements CommonControllerInterface {
	static Scanner sc = new Scanner(System.in);
	ProductService productService = new ProductService();
	OrdersDAO ordersDAO = new OrdersDAO();
	@Override
	public void execute() {
		boolean isStop =false;
		System.out.println("===상품 조회 페이지===");
		while (!isStop) {
			selectMenuDisplay();
			int job = sc.nextInt();
			sc.nextLine(); 
			isStop = f_productSelect(job, isStop, null);
		}
	}
	


	public void execute(MembersDTO member) {
		System.out.println("===쇼핑 페이지===");
		// 회원용(매개변수 있음)
		boolean isStop = false;
		boolean isSelectStop = false;
		if (member.getMemeberRole().equalsIgnoreCase("User")) {
			while (!isStop) {
				menuDisplay(member.getMemeberRole());
				int job = sc.nextInt();
				sc.nextLine();
				switch (job) {
					case 1 -> {
						System.out.println("===상품 조회 페이지===");
						while (!isSelectStop) {
							selectMenuDisplay();
							int job2 = sc.nextInt();
							sc.nextLine(); 
							isSelectStop = f_productSelect(job2, isSelectStop, member);
						}
						isSelectStop = false;
					}
					//장바구니
					case 2 -> {new CartController().execute(member);}
					//리뷰
					case 3 -> {new ReviewController().execute(member);}
					//주문내역
					case 4 -> {new OrdersController().execute(member);}
					case 5 -> {new MembersController().execute(member);}
					case 6 -> {member=null;  FrontController.main(null);}
					case 7 ->{return;}
				}
			}
		} else {
			while (!isStop) {
				menuDisplay(member.getMemeberRole());
				int job = sc.nextInt();
				switch (job) {
				case 1 -> {
					new MembersController().execute(member);
				}
				//리뷰
				case 2 -> {
					new ReviewController().execute(member);
				}
				case 3 -> {
					f_selectAll(member);
				}
				case 4 -> {
					f_selectName();
				}
				//상품등록
				case 5 -> {
					f_insertProduct();
				}
				//재고
				case 6 -> {
					f_updateInventory();
				}
				case 7 -> {
					f_delete();
				}
				case 8 ->{f_selectOrderAll();}
				case 9 ->{f_selectOrderByName(member);}
				case 10 -> {
					member=null;  FrontController.main(null);
				}
				case 11 ->{return;}
			}
			}
		}
	}
	
	private void f_selectOrderByName(MembersDTO member) {
		f_selectAll(member);
		System.out.print("주문 목록 제품명 입력: ");
		String name = sc.next();
		List<OrdersDTO> olist = new ArrayList<>();
		olist = ordersDAO.selectByPro(name);
		OrdersView.display(olist);
	}

	private void f_selectOrderAll() {
		List<OrdersDTO> olist = new ArrayList<>();
		olist = ordersDAO.selectAll();
		OrdersView.display(olist);
	}

	private void f_insertProduct() {
		System.out.print("제품명: ");
		String productName = sc.next();
		System.out.print("가격: ");
		int price = sc.nextInt();
		System.out.print("출시일: ");
		String date = sc.next();
		Date d = null;
		if(!date.equals("0")) {
			d = DateUtil.convertTosqlDate(DateUtil.convertToDate(date));
		}
		System.out.print("재고: ");
		int inventory = sc.nextInt();
		int isActive = 0;
		if(inventory > 0) {isActive = 1;}
		System.out.print("카테고리: ");
		String cate = sc.next();
		System.out.print("브랜드명: ");
		String brandName = sc.next();
		
		ProductDTO product = ProductDTO.builder()
				.productName(productName)
				.brandName(brandName)
				.cate(cate)
				.createdAt(d)
				.price(price)
				.inventory(inventory)
				.totalSales(0)
				.isActive(isActive)
				.avgRating(0.0)
				.build();
		
		int result = productService.insertProduct(product);
		ProductView.display(result+"건: " + product.getProductName() + " 추가");
	}

	private void f_updateInventory() {
		int result = 0;
		System.out.print("재고 관리 상품명>");
		String name = sc.next();
		ProductDTO product = productService.selectByName(name);
		if(product == null) {
			ProductView.display("이름 확인 필요...");
			return;
		}
		System.out.print("수량 입력>");
		int num = sc.nextInt();
		num += product.getInventory();
		if(num >= 0) {
			result = productService.updateInventory(product,num);
		}
		if(result > 0) {
			ProductView.display(name + " 재고 관리 완료!");
		}else {
			ProductView.display("재고 관리 실패... 수량을 확인하세요!");
		}
	}
	
	private void f_delete() {
		System.out.print("삭제할 상품 이름>");
		String name = sc.next();
		ProductDTO product = productService.selectByName(name);
		if(product == null) {
			ProductView.display("이름 확인 필요...");
			return;
		}
		int result = productService.deleteByName(name);
	
		if(result > 0) {
			ProductView.display(name + " 삭제 완료!");
		}else {
			ProductView.display("삭제 실패...");
		}
	}
	
	private void f_selectBrand() {
		String target = "brand_name";
		List<String> slist = productService.selectList(target);
		ProductView.display(slist, target);
		System.out.println();
		System.out.print("조회할 브랜드 입력>>");
		String input = sc.next();
		
		List<ProductDTO> plist = productService.selectBy2(target, input);
		ProductView.display(plist);
	}
	
	private void f_selectCate() {
		String target = "cate";
		List<String> slist = productService.selectList(target);
		ProductView.display(slist, target);
		System.out.println();
		System.out.print("조회할 카테고리 입력>>");
		String input = sc.next();
		
		List<ProductDTO> plist = productService.selectBy2(target, input);
		ProductView.display(plist);
	}
	
	public void f_selectAll(MembersDTO member) {
		List<ProductDTO> plist = productService.selectAll(member);
		ProductView.display(plist);
	}
	
	private void f_selectGroup(String ordermenu) {
		List<ProductDTO> plist = productService.selectAll(ordermenu);
		ProductView.display(plist);
	}
	
	private void f_selectKeyword() {
		System.out.print("키워드 입력>");
		String input = sc.next();
		List<ProductDTO> plist = productService.selectByKeyword(input);
		ProductView.display(plist);
	}
	
	private void f_selectName() {
		System.out.print("제품명 입력>");
		String input = sc.next();
		ProductDTO product = productService.selectByName(input);
		ProductView.display(product);
	}
	
	private boolean f_productSelect(int job, boolean isStop, MembersDTO member) {
		switch (job) {
			case 1-> {f_selectAll(member); return false;}
			case 2-> {f_selectCate(); return false;}
			case 3-> {f_selectGroup("created_at"); return false;}
			case 4-> {f_selectGroup("total_sales"); return false;}
			case 5-> {f_selectGroup("avg_rating"); return false;}
			case 6-> {f_selectBrand(); return false;}
			case 7-> {f_selectKeyword(); return false;}
			case 8-> {f_selectName(); return false;}
			case 9-> {return true;}
			default -> {return false;}
		}
	}
	
	
	private void menuDisplay(String memeberRole) {
		if (memeberRole.equalsIgnoreCase("User")) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println("1. 상품조회 | 2. 장바구니 | 3. 리뷰 | 4. 주문 \n 5.내 정보 | 6. 로그아웃 | 7. 되돌아가기");
			System.out.println("-------------------------------------------------------------------------------");
			System.out.print("작업 선택>");
		} else {
			System.out.println("----------------------------------------------------------------------------");
			System.out.println("1. 회원관리 | 2. 리뷰관리 | 3. 모든 상품 조회 | 4. 상품 조회(상품명)\n 5. 상품 등록 6. 상품 제고 관리| 7.상품 삭제 | 8.전체 주문 목록 | 9. 주문 목록 조회(상품명) \n 10.로그아웃 | 11.되돌아가기");
			System.out.println("----------------------------------------------------------------------------");
			System.out.print("작업 선택>");
		}
	}

	private void selectMenuDisplay() {
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("1. 모든 상품 조회 | 2. 상품 조회(카테고리) | 3. 상품 조회(최신순) | 4. 상품 조회(인기순) | 5.상품 조회(평점순) \n6. 상품 조회(브랜드) | 7. 상품 검색(키워드) | 8.상품 검색(상품명) | 9. 되돌아가기");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.print("작업 선택>");
	}
}
