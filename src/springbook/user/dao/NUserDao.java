package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import springbook.user.domain.User;

public class NUserDao extends UserDao {
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.mariadb.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mariadb://localhost:3306/study", "root", "kmk75042");
		
		return c;
	}
}
