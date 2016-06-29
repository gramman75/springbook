package springbook.user.service;

import java.util.List;

import springbook.user.dao.UserSpringDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	UserSpringDao userDao;
	
	public void setUserDao(UserSpringDao userDao){
		this.userDao = userDao;
	}
	
	public void upgradeLevels(){
		List<User> users = userDao.getAll();
		
		for(User user: users){
			Boolean changed = null;
			
			if (user.getLevel() == Level.BASIC && user.getLogin() >= 50){
				user.setLevel(Level.SILVER);
				changed = true;
			} else if (user.getLevel() == Level.SILVER && user.getRecommend()>= 30){
				user.setLevel(Level.GOLD);
				changed = true;
			} else {
				changed = false;
			}
			
			if (changed) {userDao.update(user);}
		}
		
	}
}