package springbook.user.service;


import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import springbook.user.dao.UserSpringDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	UserSpringDao userDao;
	private DataSource dataSource;
	private PlatformTransactionManager transactionManager;
	private MailSender mailSender;
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public void setUserDao(UserSpringDao userDao){
		this.userDao = userDao;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager){
		this.transactionManager = transactionManager;
	}
	
	public void setMailSender(MailSender mailSender){
		this.mailSender = mailSender;
	}
	
	public void add(User user){
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		
		userDao.add(user);
	}
	
	public void upgradeLevels() throws Exception{
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		
		try{
			upgradeLevelsInternal();
			this.transactionManager.commit(status);
		} catch(RuntimeException e){
			this.transactionManager.rollback(status);
			throw e;
		} 
		
	}
	
	private void upgradeLevelsInternal(){
		
		List<User> users = userDao.getAll();
		
		for(User user: users){
			if (canUpgradeLevel(user)){
				upgradeLevel(user);
			}
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
		sendUpgradeEMail(user);
	}
	
	private void sendUpgradeEMail(User user){
//		Properties props = new Properties();
//		props.put("mail.smtp.host", "mail.ksug.orgi");
//		
//		Session s = Session.getInstance(props, null);
//		
//		MimeMessage message = new MimeMessage(s);
//		try{
//			message.setFrom(new InternetAddress("gramman75@gmail.com"));
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//			message.setSubject("Upgrade Level");
//			message.setText("your level upgraded to" + user.getLevel().name());
//		} catch (AddressException e){
//			throw new RuntimeException(e);
//		} catch (MessagingException e){
//			throw new RuntimeException(e);
//		}
		
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("gramman75@gmail.com");
		mailMessage.setSubject("Upgrade Level");
		mailMessage.setText("your level upgraded to " + user.getLevel().name());
		
		this.mailSender.send(mailMessage);
	}
}