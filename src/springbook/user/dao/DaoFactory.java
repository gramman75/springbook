package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springbook.user.dao.*;
import springbook.user.domain.*;

@Configuration
public class DaoFactory {
	@Bean
	public UserDao userDao(){		
		UserDao userDao = new UserDao(connectionMaker());
		
		return userDao;
	}
	@Bean
	public ConnectionMaker connectionMaker(){
		return new NConnectionMaker();
	}

}
