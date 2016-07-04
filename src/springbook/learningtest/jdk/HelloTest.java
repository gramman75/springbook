package springbook.learningtest.jdk;

import org.junit.Test;
import static org.junit.Assert.assertThat;
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
}
