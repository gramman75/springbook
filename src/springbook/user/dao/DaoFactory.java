package springbook.user.dao;
			
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springbook.user.dao.*;
import springbook.user.domain.*;

@Configuration
public class DaoFactory {
	private ConnectionMaker connectionMaker;
	
	public void setConnectionMaker(ConnectionMaker connectionMaker){
		this.connectionMaker = connectionMaker;
	}
	
	@Bean
	public UserDao userDao(){		
		UserDao userDao = new UserDao(this.connectionMaker);
		use
		return userDao;
	}
	@Bean
	public ConnectionMaker connectionMaker(){
		return new NConnectionMaker();
	}
	
}
