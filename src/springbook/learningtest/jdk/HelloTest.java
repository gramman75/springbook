package springbook.learningtest.jdk;

import org.junit.Test;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;

public class HelloTest {

	@Test
	public void simpleProxy(){
		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		String name = "Kim";
		
		assertThat(proxiedHello.sayHello(name),is("HELLO "+name.toUpperCase()));
		assertThat(proxiedHello.sayHi(name),is("HI "+name.toUpperCase()));
		assertThat(proxiedHello.sayThankYou(name),is("THANK YOU "+name.toUpperCase()));
		
	}
	
	@Test
	public void dynamicProxy(){
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {Hello.class}, 
				new UppercaseHandler(new HelloTarget())
		);
		
		String name = "Kim";
		
		assertThat(proxiedHello.sayHello(name),is("HELLO "+name.toUpperCase()));
		assertThat(proxiedHello.sayHi(name),is("HI "+name.toUpperCase()));
		assertThat(proxiedHello.sayThankYou(name),is("THANK YOU "+name.toUpperCase()));
		
	}
}
