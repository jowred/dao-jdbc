package application;

import java.util.Date;
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
		System.out.println();
		
		System.out.println("---- Seller findAll ----");
		sellers = sellerDAO.findAll();
		sellers.stream().forEach(System.out::println);
		System.out.println();
		
		System.out.println("---- Seller insert ----");
		Seller s = new Seller(null, "Terry Bogard", "terry.bogard@gmail.com", new Date(), 4000.0, dep);
		sellerDAO.insert(s);
		System.out.println("Inserido. Novo ID = " + s.getId());
	}

}
