package springbook.user.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import springbook.user.dao.*;
import springbook.user.domain.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")

@DirtiesContext
public class UserSpringDaoTest {
	

//	public static void main(String[] args) throws ClassNotFoundException, SQLException {
	
//	@Autowired
//	private ApplicationContext context;
	
	@Autowired
	UserSpringDao dao;
	
//	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setup(){
		ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
		this.dao = context.getBean("userSpringDao", UserSpringDao.class);
//		DataSource dataSource = new SingleConnectionDataSource(
//				"jdbc:mariadb://localhost:3306/study", "root","", true);
//		
//		dao.setDataSource(dataSource);
		
		this.user1 = new User("1", "kim1","kim1",Level.BASIC, 1,0);
		this.user2 = new User("2", "kim2","kim2",Level.SILVER,55,10);
		this.user3 = new User("3", "kim3","kim3",Level.GOLD, 100,40);
		
		
	}

	@Test
	public void addAndGet() throws SQLException{
		
//		ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//		UserDao dao = context.getBean("userDao", UserDao.class);
		User user = new User();
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(),is(1));
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);

	}
	
	@Test
	public void count() throws SQLException{
//		ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
//		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		
		assertThat(dao.getCount(), is(0));
		
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException{
//		ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
//		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.get("id");
	}
	
	@Test
	public void getAll() throws SQLException{
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(),is(1));
		checkSameUser(user1, users1.get(0));
		
		
		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(),is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));

		
		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(),is(3));
		checkSameUser(user1, users3.get(0));
		checkSameUser(user2, users3.get(1));
		checkSameUser(user3, users3.get(2));
		
	}
	
	@Test(expected=DataAccessException.class)
	public void duplicateKey(){
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}
	
	@Test
	public void update(){
		dao.deleteAll();
		
		dao.add(user1);
		user1.setName("moon");
		user1.setLevel(Level.BASIC);
		user1.setLogin(100);
		user1.setPassword("1234");
		user1.setRecommend(400);
	
		dao.update(user1);
		
		User updateUser1 = dao.get(user1.getId());
		
		checkSameUser(user1, updateUser1);
		
	}
	
	private void checkSameUser(User user1, User user2){
		assertThat(user1.getId(),is(user2.getId()));
		assertThat(user1.getName(),is(user2.getName()));
		assertThat(user1.getPassword(),is(user2.getPassword()));
		assertThat(user1.getLevel(),is(user2.getLevel()));
		assertThat(user1.getLogin(),is(user2.getLogin()));
		assertThat(user1.getRecommend(),is(user2.getRecommend()));

	}
	
	
}
