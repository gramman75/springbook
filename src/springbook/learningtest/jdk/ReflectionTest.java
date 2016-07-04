package springbook.learningtest.jdk;

import java.lang.reflect.Method;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionTest {
	
	@Test
	public void invokeMethod() throws Exception{
		String name ="kim";
		Method method = name.getClass().getMethod("length");
		
		int len = (int) method.invoke(name);
		assertThat(len,is(3));
	}
}