package springbook.user.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
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
public class UserDaoTest {
	

//	public static void main(String[] args) throws ClassNotFoundException, SQLException {
	
//	@Autowired
//	private ApplicationContext context;
	
	@Autowired
	UserDao dao;
	
//	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setup(){
//		ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
//		this.dao = this.context.getBean("userDao", UserDao.class);
		DataSource dataSource = new SingleConnectionDataSource(
				"jdbc:mariadb://localhost:3306/testdb", "root","kmk75042", true);
		
		dao.setDataSource(dataSource);
		
		System.out.println(this.dao);
		this.user1 = new User("1", "kim1","kim1");
		this.user2 = new User("2", "kim2","kim2");
		this.user3 = new User("3", "kim3","kim3");
		
	}

	@Test
	public void addAndGet() throws SQLException{
		
//		ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//		UserDao dao = context.getBean("userDao", UserDao.class);
		User user = new User();
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		user.setId("15");
		user.setName("kim");
		user.setPassword("kmk");
		
		dao.add(user);
		assertThat(dao.getCount(),is(1));
		
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));

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
}
