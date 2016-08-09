package springbook.learningtest.ioc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class HelloTest {
	
	@Autowired
	private Hello hello;
	
	@Autowired
	private Printer printer;

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
		
		Hello hello = ac.getBean("hello", Hello.class);
//		hello.setName("Spring");
		hello.print();

		assertThat(ac.getBean("printer").toString(),is("Hello Windows 7"));
	}
	
//	@Test
//	public void autoWired(){
//		GenericXmlApplicationContext ac = new GenericXmlApplicationContext("springbook/learningtest/ioc/applicationContext.xml");
//		
//		this.hello.setName("Spring");
//		assertThat(this.printer.toString(),is("Hello Spring"));
//	}
	@Test
	public void singletonScope(){
		ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
		
		Set<SingletonBean> beans = new HashSet<SingletonBean>();
		beans.add(ac.getBean(SingletonBean.class));
		beans.add(ac.getBean(SingletonBean.class));
		
		assertThat(beans.size(),is(2));
	}
	
	@Scope("prototype")
	static class SingletonBean{}
	
	static class SingletonClientBean{
		@Autowired SingletonBean bean1;
		@Autowired SingletonBean bean2;
		
	}
	
}
