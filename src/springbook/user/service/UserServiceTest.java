package springbook.user.service;

import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserSpringDao;
import springbook.user.dao.UserSpringDaoJdbc;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	@Autowired
	ApplicationContext context;

	@Autowired
	UserService userService;
	
//	@Autowired
//	UserServiceImpl userServiceImpl;
	
	@Autowired
	UserService testUserService;
	
	
	@Autowired
	UserSpringDaoJdbc userDao;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	MailSender mailSender;
	
	
	List<User> users;
	
	@Before
	public void setup(){
		users = Arrays.asList(
			new User("kim1","��1","p1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1,0,"kim1@email.com"),
			new User("kim2","��2","p1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,0, "kim2@email.com"),
			new User("kim3","��3","p1",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD-1,"kim3@email.com"),
			new User("kim4","��4","p1",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD,"kim4@email.com"),
			new User("kim5","��5","p1",Level.GOLD, 10,10,"kim5@email.com")
		);
	}
	
	@Test
	public void add(){
		userDao.deleteAll();
		User userWithLevel = new User("kim1","��5","p1",Level.GOLD, 10,10,"kim5@email.com");
		User userWithoutLevel = new User("kim5","��5","p1",null, 10,10,"kim5@email.com");
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelAdd = userDao.get(userWithLevel.getId());
		User userWithoutLevelAdd = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelAdd.getLevel(),is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelAdd.getLevel(),is(Level.BASIC));
		
		
	}
	
	@Test
	public void bean(){
		assertThat(this.userService,is(notNullValue()));
	}
	
	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		UserSpringDao mockUserSpringDao = mock(UserSpringDao.class); 
		when(mockUserSpringDao.getAll()).thenReturn(this.users);

		MockMailSender mailSender = new MockMailSender();

		userServiceImpl.setMailSender(mailSender);
		userServiceImpl.setUserDao(mockUserSpringDao);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserSpringDao,times(2)).update(any(User.class));
		verify(mockUserSpringDao).update(users.get(1));
		assertThat(users.get(1).getLevel(),is(Level.SILVER));
		
		
		
		
		
		List<String> request = mailSender.getRequest();
		assertThat(request.size(),is(2));
		
		assertThat(request.get(0),is(users.get(1).getEmail()));
		assertThat(request.get(1),is(users.get(3).getEmail()));
		
	}
	
	private void checkLevel(User user, Level level){
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(level));
	}
	
	private void checkLevelUpgrade(User user,boolean upgraded){
		User userUpgrade = userDao.get(user.getId());
		if (upgraded){
			assertThat(userUpgrade.getLevel(),is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpgrade.getLevel(),is(user.getLevel()));
		}
	}
	
	static class TestUserService extends UserServiceImpl{
		private String id ="kim4";
		
		
		protected void upgradeLevel(User user){
			if (user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
		public List<User> getAll(){
			for(User user: super.getAll()){
				super.update(user);
			}
			
			return null;
		}
	}
	
	static class TestUserServiceException extends RuntimeException{
		
	}

	/*
	@Test
	public void upgradeAllorNothing() throws Exception{
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setMailSender(this.mailSender);
	
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);

		userDao.deleteAll();
		
		for(User user: users) userDao.add(user);
		
		try{
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected!");
		} catch(TestUserServiceException e){
			
		} 
		
		checkLevelUpgrade(users.get(1), false);
	}
	
	@Test
	public void upgradeAllorNothingProxy() throws UndeclaredThrowableException{
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(testUserService);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern("upgradeLevels");
		
		UserService userService =  (UserService)Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] {UserService.class},
				txHandler
				);
		userDao.deleteAll();
		testUserService.setUserDao(this.userDao);
		testUserService.setMailSender(this.mailSender);
		
		for(User user: users) userDao.add(user);
		
		try{
			userService.upgradeLevels();
			fail("TestUserServiceException expected!");
		} catch(TestUserServiceException e){
			
		} 
		
		checkLevelUpgrade(users.get(1), false);
		
	}
	*/
	@Test
	@DirtiesContext
	public void upgradeAllorNothingFactoryBean() throws Exception{
//		TestUserService testUserService = new TestUserService(users.get(3).getId());
//		testUserService.setMailSender(mailSender);
//		testUserService.setUserDao(userDao);
//		
//		ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService",ProxyFactoryBean.class);
//		txProxyFactoryBean.setTarget(testUserService);
//		
//		UserService txUserService = (UserService) txProxyFactoryBean.getObject();
//		txUserService.upgradeLevels();
		
		userDao.deleteAll();
		
		for(User user: users) userDao.add(user);
		
		try{
//			txUserService.upgradeLevels();
			this.testUserService.upgradeLevels();
			fail("TestUserServiceException expected!");
		} catch(TestUserServiceException e){
			
		} 
		
		checkLevelUpgrade(users.get(1), false);
		
	}
	
	@Test
	public void advisorAutoProxy() throws Exception{
		System.out.println(UserServiceImpl.class.getMethod("setUserDao", UserSpringDao.class));
		assertThat(testUserService,is(java.lang.reflect.Proxy.class));
	}
	
	@Test(expected=TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttriubte(){

		testUserService.getAll();
	}
	
	@Test
	@Transactional	
	@Rollback(false)
	public void transactionSync(){
//		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
//		txDefinition.setReadOnly(true);
//		TransactionStatus status = transactionManager.getTransaction(txDefinition);
//		
		
		userService.deleteAll();

		userService.add(users.get(1));
		userService.add(users.get(0));
//		transactionManager.commit(status);
		
	}
}
