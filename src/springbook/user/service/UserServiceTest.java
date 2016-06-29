package springbook.user.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserSpringDaoJdbc;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {

	@Autowired
	UserService userService;
	
	@Autowired
	UserSpringDaoJdbc userDao;
	
	List<User> users;
	
	@Before
	public void setup(){
		users = Arrays.asList(
				new User("kim1","±è1","p1",Level.BASIC, 49,0),
				new User("kim2","±è2","p1",Level.BASIC, 59,0),
				new User("kim3","±è3","p1",Level.SILVER, 60,29),
				new User("kim4","±è4","p1",Level.SILVER, 60,30),
				new User("kim5","±è5","p1",Level.GOLD, 10,10)
				);
	}
	
	@Test
	public void bean(){
		assertThat(this.userService,is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels(){
		userDao.deleteAll();
		for(User user: users){userDao.add(user);};
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0),Level.BASIC);
		checkLevel(users.get(1),Level.SILVER);
		checkLevel(users.get(2),Level.SILVER);
		checkLevel(users.get(3),Level.GOLD);
		checkLevel(users.get(4),Level.GOLD);
		
		
	}
	
	private void checkLevel(User user, Level level){
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(level));
	}
}
