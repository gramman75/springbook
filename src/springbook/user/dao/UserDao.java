package springbook.user.dao;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;
//import springbook.user.dao.SimpleConnectionMaker;
import springbook.user.dao.ConnectionMaker;

public class UserDao {
//	private SimpleConnectionMaker simpleConnectionMaker;
	
//	private ConnectionMaker connectionMaker;
//	public UserDao(ConnectionMaker connectionMaker){
//		this.connectionMaker= connectionMaker;
//	}
	
//	public void setConnectionMaker(ConnectionMaker connectionMaker){
//		this.connectionMaker = connectionMaker;
//	}
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	public void add(User user) throws SQLException{
//		Connection c = connectionMaker.makeConnection();
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("insert into users(userid, name, password) values (?,?,?)");
		
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
		
	}
	
	public User get(String id) throws SQLException{
//		Connection c = connectionMaker.makeConnection();
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where userid = ?");
		ps.setString(1,  id);
		
		ResultSet rs = ps.executeQuery();
		User user = null;
		if(rs.next()){
			user = new User();
			user.setId(rs.getString("userid"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if (user == null) throw new EmptyResultDataAccessException(1);
		
		return user;
	}
	
	public void deleteAll() throws SQLException{
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("delete from users");
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public int getCount() throws SQLException{
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement("select count(*) from users");
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		int count = rs.getInt(1);
		
		rs.close();
		ps.close();
		c.close();
		
		return count;
		
	}
	
//	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
	
//	private Connection getConnection() throws ClassNotFoundException, SQLException {
//		Class.forName("org.mariadb.jdbc.Driver");
//		Connection c = DriverManager.getConnection("jdbc:mariadb://localhost:3306/study", "root", "kmk75042");
//		
//		return c;
//	}
//	
//	public static void main(String argv[]) throws ClassNotFoundException, SQLException {
//		UserDao dao = new UserDao();
//		
//		User user = new User();
//		
//		user.setId("2");
//		user.setName("kim");
//		user.setPassword("kmk");
//		
//		dao.add(user);
//		
//		System.out.println(user.getId() + " : Success");
//		
//		User user2 = dao.get(user.getId());
//		System.out.println(user2.getName());
//		
//
//	}
}
