package springbook.learningtest.jdk;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

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
	
	@Test
	public void proxyFactoryBean(){
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		String name = "Kim";
		
		assertThat(proxiedHello.sayHello(name),is("HELLO "+name.toUpperCase()));
		assertThat(proxiedHello.sayHi(name),is("HI "+name.toUpperCase()));
		assertThat(proxiedHello.sayThankYou(name),is("THANK YOU "+name.toUpperCase()));
	}
	
	@Test
	public void pointcutAdvisor(){
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

		pointcut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxiedHello = (Hello) pfBean.getObject();
	
		String name = "Kim";
		
		assertThat(proxiedHello.sayHello(name),is("HELLO "+name.toUpperCase()));
		assertThat(proxiedHello.sayHi(name),is("HI "+name.toUpperCase()));
		assertThat(proxiedHello.sayThankYou(name),is("Thank You "+name));
		
	}
	
	static class UppercaseAdvice implements MethodInterceptor{

		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}
	
	}
}
