package model.dao.impl;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class SellerDAOJDBC implements SellerDAO {
	
	private Connection conn;
	
	public SellerDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller s) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO seller "
					+ "(name, email, birth_date, base_salary, department_id) "
					+ "VALUES (?, ?, ?, ?, ?)",
					RETURN_GENERATED_KEYS); // Retorna o ID do vendedor inserido
			st.setString(1, s.getName());
			st.setString(2, s.getEmail());
			st.setDate(3, new java.sql.Date(s.getBirthDate().getTime()));
			st.setDouble(4, s.getSalary());
			st.setInt(5, s.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1); // Primeira coluna da tabela, retornada no ResultSet
					s.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DBException("Erro inesperado: nenhuma inserção realizada.");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller s) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE seller SET "
					+ "name = ?, email = ?, birth_date = ?, base_salary = ?, department_id = ? "
					+ "WHERE id = ?"); // Retorna o ID do vendedor inserido
			st.setString(1, s.getName());
			st.setString(2, s.getEmail());
			st.setDate(3, new java.sql.Date(s.getBirthDate().getTime()));
			st.setDouble(4, s.getSalary());
			st.setInt(5, s.getDepartment().getId());
			st.setInt(6, s.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT seller.*, department.name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.department_id = department.id "
					+ "WHERE seller.id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			// Se há resultados na consulta
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				return intantiateSeller(rs, dep);
			}
			return null;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller intantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("id"));
		seller.setName(rs.getString("name"));
		seller.setEmail(rs.getString("email"));
		seller.setSalary(rs.getDouble("base_salary"));
		seller.setBirthDate(rs.getDate("birth_date"));
		seller.setDepartment(dep);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		return new Department(rs.getInt("department_id"), rs.getString("DepName"));
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT seller.*, department.name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department "
					+ "ON seller.department_id = department.id "
					+ "ORDER BY name");
			
			rs = st.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			
			// Mapa para manter a referência do departamento para o mesmo objeto, de acordo com seu ID
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				Department dep = map.get(rs.getInt("department_id"));
				
				if (dep == null) {
					dep = this.instantiateDepartment(rs);
					map.put(rs.getInt("department_id"), dep);
				}
				
				Seller seller = intantiateSeller(rs, dep);
				sellers.add(seller);
			}
			return sellers;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT seller.*, department.name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department "
					+ "ON seller.department_id = department.id "
					+ "WHERE Department_id = ? "
					+ "ORDER BY name");
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> sellers = new ArrayList<>();
			
			// Mapa para manter a referência do departamento para o mesmo objeto, de acordo com seu ID
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				Department dep = map.get(rs.getInt("department_id"));
				
				if (dep == null) {
					dep = this.instantiateDepartment(rs);
					map.put(rs.getInt("department_id"), dep);
				}
				
				Seller seller = intantiateSeller(rs, dep);
				sellers.add(seller);
			}
			return sellers;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
