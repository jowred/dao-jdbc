package application;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	
	public static void main(String[] args) {
		
		SellerDAO sellerDAO = DAOFactory.createSellerDAO();
		
		System.out.println("---- Seller findById ----");
		Seller seller = sellerDAO.findById(3);
		System.out.println(seller);
		System.out.println();
		
		System.out.println("---- Seller findByDepartment ----");
		Department dep = new Department(2, null);
		List<Seller> sellers = sellerDAO.findByDepartment(dep);
		sellers.stream().forEach(System.out::println);
	}

}
