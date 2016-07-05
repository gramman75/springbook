package springbook.learningtest.jdk;

import java.lang.reflect.Method;

public interface InvocationHandler {
	public Object invoke(Object proxy, Method method, Object[] args) throws Exception;
}
