package springbook.learningtest.ioc;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class HelloTest {

	@Test
	public void applicationContext(){
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerSingleton("hello1", Hello.class);
		
		Hello hello1 = (Hello) ac.getBean("hello1");
		
		assertThat(hello1,is(notNullValue()));
	}
	
	@Test
	public void beanDefinition(){
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerSingleton("hello1", Hello.class);
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		ac.registerBeanDefinition("hello2", helloDef);
		
		Hello hello2 = (Hello) ac.getBean("hello2");
		assertThat(hello2.sayHello(),is("Hello Spring"));
		assertThat(ac.getBeanFactory().getBeanDefinitionCount(),is(2));
	}
	
	@Test
	public void registerBeanWithDependency(){

		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerBeanDefinition("printer", new RootBeanDefinition(ConsolePrinter.class));
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
		
		ac.registerBeanDefinition("hello1", helloDef);
		
		Hello hello1 = (Hello) ac.getBean("hello1");
		hello1.print();
		
		assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
	}
	
	@Test
	public void genericApplicationContext(){
		GenericApplicationContext ac = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
		
		reader.loadBeanDefinitions("springbook/learningtest/ioc/applicationContext.xml");
		ac.refresh();
		
		Hello hello = (Hello) ac.getBean("hello");
		hello.print();
		
		assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
	}
	
}
