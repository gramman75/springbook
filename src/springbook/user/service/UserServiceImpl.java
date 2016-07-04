package springbook.user.service;

import springbook.user.domain.User;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import springbook.user.dao.UserSpringDao;
import springbook.user.domain.Level;

public class UserServiceImpl implements UserService {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	UserSpringDao userDao;
//	private DataSource dataSource;
	private MailSender mailSender;
	
//	public void setDataSource(DataSource dataSource){
//		this.dataSource = dataSource;
//	}
		
	public void setUserDao(UserSpringDao userDao){
		this.userDao = userDao;
	}
	
	public void setMailSender(MailSender mailSender){
		this.mailSender = mailSender;
	}
	
	public void upgradeLevels(){
		
		List<User> users = userDao.getAll();
		
		for(User user: users){
			if (canUpgradeLevel(user)){
				upgradeLevel(user);
			}
		}
	}
	
	public void add(User user){
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		
		userDao.add(user);
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
		sendUpgradeEMail(user);
	}
	
	public void sendUpgradeEMail(User user){
			
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("gramman75@gmail.com");
		mailMessage.setSubject("Upgrade Level");
		mailMessage.setText("your level upgraded to " + user.getLevel().name());
		
		this.mailSender.send(mailMessage);
	}

}
