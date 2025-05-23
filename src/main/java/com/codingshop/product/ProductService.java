package com.codingshop.product;

import java.util.List;

import com.codingshop.members.MembersDTO;

public class ProductService {
	ProductDAO productDAO = new ProductDAO();
	
	public List<String> selectList(String target){
		return productDAO.selectList(target);
	}
	
	public List<ProductDTO> selectBy2(String target, String input){
		return productDAO.selectBy2(target, input); 
	}
	
	public ProductDTO selectByName(String name) {
		return productDAO.selectByName(name);
	}
	
	public List<ProductDTO> selectByKeyword(String input) {
		return productDAO.selectByKeyword(input);
	}
	
	public List<ProductDTO> selectAll(MembersDTO member){
		return productDAO.selectAll(member);
	}
	public List<ProductDTO> selectAll(String orderMenu){
		return productDAO.selectAll(orderMenu);
	}
	
	public int updateInventory(ProductDTO product, int num) {
		return productDAO.updateInventory(product, num);
	}
	
	public int insertProduct(ProductDTO product) {
		return productDAO.insertProduct(product);
	}
	
	public int deleteByName(String name) {
		return productDAO.deleteByName(name);
	}
}
