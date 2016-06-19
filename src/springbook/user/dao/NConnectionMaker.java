package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {
	public Connection makeConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.mariadb.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mariadb://localhost:3306/study", "root", "");
		
		return c;
	}
}
