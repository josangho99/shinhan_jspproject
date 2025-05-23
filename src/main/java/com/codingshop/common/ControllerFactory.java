package com.codingshop.common;

import com.codingshop.members.MembersController;
import com.codingshop.members.MembersSignUpController;
import com.codingshop.product.ProductController;
//Factory pattern
public class ControllerFactory {

	@SuppressWarnings("null")
	public static CommonControllerInterface make(String business) {
		CommonControllerInterface controller = null;
		switch (business){
		case "login"->{controller = new MembersController();}
		case "signup" ->{controller = new MembersSignUpController();}
		case "shopping" ->{controller = new ProductController();}
	}
		return controller;
}
}