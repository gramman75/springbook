package springbook.learningtest.factorybean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FactoryBeanTest {
	
	@Autowired
	ApplicationContext context;
	
//	@Autowired
//	Object message;
	
	@Test
	public void getMessageFromFactoryBean(){
		Object message = context.getBean("message");
		
		assertThat(message,is(Message.class));
		assertThat(((Message) message).getText(),is("Factory Bean"));
	}
	

}
