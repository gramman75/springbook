package springbook.user.dao;
			
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import javax.sql.DataSource;

import springbook.user.dao.*;
import springbook.user.domain.*;

@Configuration
public class DaoFactory  {
	
	@Bean
	public DataSource dataSource(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mariadb://localhost:3306/study");
		dataSource.setUsername("root");
		dataSource.setPassword("kmk75042");
		
		return dataSource;
	}
	
	@Bean
	public UserDao userDao(){		
		UserDao userDao = new UserDao();
//		userDao.setConnectionMaker(connectionMaker());
		userDao.setDataSource(dataSource());
		return userDao;
	}
//	@Bean
//	public ConnectionMaker connectionMaker(){
//		return new NConnectionMaker();
//	}
	
}
