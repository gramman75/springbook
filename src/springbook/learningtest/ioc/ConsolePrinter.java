package springbook.learningtest.ioc;

import org.springframework.stereotype.Component;

@Component("printer")
public class ConsolePrinter implements Printer {

	private String message;
	
	@Override
	public void print(String message) {
		this.message = message;
		System.out.println(this.message);

	}
	
	public String toString(){
		return this.message;
	}

}
