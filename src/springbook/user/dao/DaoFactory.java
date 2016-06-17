package springbook.user.dao;

import springbook.user.dao.*;
import springbook.user.domain.*;

public class DaoFactory {
	public UserDao userDao(){		
		UserDao userDao = new UserDao(connectionMaker());
		
		return userDao;
	}
	
	public ConnectionMaker connectionMaker(){
		return new NConnectionMaker();
	}

}
