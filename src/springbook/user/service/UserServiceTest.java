package springbook.user.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserSpringDaoJdbc;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {

	@Autowired
	UserService userService;
	
	@Autowired
	UserSpringDaoJdbc userDao;
	
	@Autowired
	DataSource dataSource;
	
	List<User> users;
	
	@Before
	public void setup(){
		users = Arrays.asList(
				new User("kim1","��1","p1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1,0),
				new User("kim2","��2","p1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,0),
				new User("kim3","��3","p1",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD-1),
				new User("kim4","��4","p1",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD),
				new User("kim5","��5","p1",Level.GOLD, 10,10)
				);
	}
	
	@Test
	public void bean(){
		assertThat(this.userService,is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception{
		userDao.deleteAll();
		for(User user: users){userDao.add(user);};
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0),Level.BASIC);
		checkLevel(users.get(1),Level.SILVER);
		checkLevel(users.get(2),Level.SILVER);
		checkLevel(users.get(3),Level.GOLD);
		checkLevel(users.get(4),Level.GOLD);
		
		checkLevelUpgrade(users.get(0),false);
		checkLevelUpgrade(users.get(1),true);
		checkLevelUpgrade(users.get(2),false);
		checkLevelUpgrade(users.get(3),true);
		checkLevelUpgrade(users.get(4),false);
		
		
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
	
	static class TestUserService extends UserService{
		private String id;
		
		private TestUserService(String id){
			this.id = id;
		}
		
		protected void upgradeLevel(User user){
			if (user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}
	
	static class TestUserServiceException extends RuntimeException{
		
	}
	
	@Test
	public void upgradeAllorNothing() throws Exception{
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setDataSource(this.dataSource);
		
		userDao.deleteAll();
		
		for(User user: users) userDao.add(user);
		
		try{
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected!");
		} catch(TestUserServiceException e){
			
		} 
		
		checkLevelUpgrade(users.get(1), false);
	}
}
