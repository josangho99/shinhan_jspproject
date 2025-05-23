package com.codingshop.common;

import java.util.Scanner;

public class FrontController {
	// FrontController는 사용자가 입력한 작업에 따라 적절한 Controller를 생성하여 execute() 메소드를 호출한다.
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean isStop = false;
		CommonControllerInterface controller = null;

		while (!isStop) {
			System.out.print("작업 선택(login, signup, shopping, end)>>");
			String job = sc.next();
			switch(job) {
				case "login"->{controller = ControllerFactory.make("login");}
				case "signup"->{controller = ControllerFactory.make("signup");}
				case "shopping"->{controller = ControllerFactory.make("shopping");}
				case "end"->{isStop = true; continue;}
				default ->{continue;}
			}
			controller.execute();//작업이 달라져도 사용법은 같다.(전략패턴)
		
		}
		
		sc.close();
		System.out.println("===MAIN END===");
		
	}	
}
	