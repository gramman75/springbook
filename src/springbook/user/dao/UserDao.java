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
	private DataSource dataSource;
	private JdbcContext jdbcContext;

	public void setDataSource(DataSource dataSource){
		this.jdbcContext = new JdbcContext();
		this.jdbcContext.setDataSource(dataSource);
		this.dataSource = dataSource;
	}
	
	
	public void add(final User user) throws SQLException{
		
//		this.jdbcContextWithStatementStrategy(
//		this.jdbcContext.workWithStatementStrategy(
//			new StatementStrategy(){
//				public PreparedStatement makePreparedStatement(Connection c) throws SQLException{
//					PreparedStatement ps = c.prepareStatement("insert into users(userid, name, password) values (?,?,?)");
//					
//					ps.setString(1, user.getId());
//					ps.setString(2, user.getName());
//					ps.setString(3, user.getPassword());
//					
//					return ps;
//				}
//			}
//		);
		
		this.jdbcContext.executeSql(
				"insert into users(userid, name, password) values (?,?,?)",
				user.getId(), user.getName(), user.getPassword()
				);
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
		this.jdbcContext.executeSql("delete from users");
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
	
//	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
//		Connection c = null;
//		PreparedStatement ps = null;
//		
//		try{
//			c = dataSource.getConnection();
//			ps = stmt.makePreparedStatement(c);
//			
//			ps.executeUpdate();
//		} catch(SQLException e){
//			throw e;
//		} finally {
//			if (ps != null) {try {ps.close();} catch(SQLException e){} }
//			if (c != null) {try {c.close();} catch(SQLException e){} }
//		}
//	}
	
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
