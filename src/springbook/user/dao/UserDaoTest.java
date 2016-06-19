package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.*;
import springbook.user.domain.*;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        ConnectionMaker connectionMaker = new NConnectionMaker();
        
//        UserDao dao = new UserDao();
//        
        DaoFactory factory = new DaoFactory();
        factory.setConnectionMaker(connectionMaker);
        
        UserDao dao = factory.userDao();
		
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//		UserDao dao1 = context.getBean("userDao", UserDao.class);
//		UserDao dao2 = context.getBean("userDao", UserDao.class);
//		
//        
//        
//		UserDao dao = new DaoFactory().userDao();
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//		UserDao dao = context.getBean("userDao", UserDao.class);
//		
//		
		User user = new User();
		
		user.setId("9");
		user.setName("kim");
		user.setPassword("kmk");
//		
		dao.add(user);
//		
		System.out.println(user.getId() + " : Success");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());

	}

}
