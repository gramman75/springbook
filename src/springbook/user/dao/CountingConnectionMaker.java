package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {
	
	int counter = 0;
	
	private ConnectionMaker realConnectionMaker;
	
	public CountingConnectionMaker(ConnectionMaker connectionMaker){
		this.realConnectionMaker = connectionMaker;
	}

	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		counter++;
		return realConnectionMaker.makeConnection();
	}
	
	public int getCounter(){
		return this.counter;
	}
}
