package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.*;
import springbook.user.domain.*;

public class CountingUserDaoTest {
	public static void main(String[] args){
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		context.getBean("userDao", UserDao.class);
		
		
		
	}

}
