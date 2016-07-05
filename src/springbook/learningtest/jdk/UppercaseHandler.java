package springbook.learningtest.jdk;

import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
	Hello target;
	
	public UppercaseHandler(Hello target){
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
		String ret = (String)method.invoke(target, args);
		return ret.toUpperCase();
	}

}
