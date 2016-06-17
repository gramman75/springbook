package springbook.user.dao;

import java.sql.SQLException;

import springbook.user.dao.*;
import springbook.user.domain.*;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        ConnectionMaker connectionMaker = new NConnectionMaker();
        
//        UserDao dao = new UserDao(connectionMaker);
        
		UserDao dao = new DaoFactory().userDao();
		
		User user = new User();
		
		user.setId("6");
		user.setName("kim");
		user.setPassword("kmk");
		
		dao.add(user);
		
		System.out.println(user.getId() + " : Success");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());

	}

}
