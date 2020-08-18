package application;

import java.util.Date;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	
	public static void main(String[] args) {
		
		Department d = new Department(1, "Books");
		System.out.println(d);
		
		Seller s = new Seller(21, "Blue Mary", "blue.mary@gmail.com", new Date(), 3000.0, d);
		System.out.println(s);
		
		SellerDAO sellerDAO = DAOFactory.createSellerDAO();
		
	}

}
