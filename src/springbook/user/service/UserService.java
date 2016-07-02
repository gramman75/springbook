package springbook.user.service;


import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import springbook.user.dao.UserSpringDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	UserSpringDao userDao;
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public void setUserDao(UserSpringDao userDao){
		this.userDao = userDao;
	}
	
	public void upgradeLevels() throws Exception{
		TransactionSynchronizationManager.initSynchronization();
		Connection c = DataSourceUtils.getConnection(this.dataSource);
		
		c.setAutoCommit(false);
		try{
			
			List<User> users = userDao.getAll();
			
			for(User user: users){
				if (canUpgradeLevel(user)){
					upgradeLevel(user);
				}
			}
			
			c.commit();
		} catch(Exception e){
			c.rollback();
			throw e;
		} finally{
			DataSourceUtils.releaseConnection(c, this.dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
		
	}
	
	private boolean canUpgradeLevel(User user){
		Level currentLevel = user.getLevel();
		
		switch(currentLevel){
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD: return false;
			default : throw new IllegalArgumentException("Unknow Level : " + currentLevel);
		}
	}
	
	protected void upgradeLevel(User user){
		user.upgradeLevel();
		userDao.update(user);
	}
}