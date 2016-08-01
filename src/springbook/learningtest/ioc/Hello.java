package springbook.learningtest.ioc;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class Hello {

	Printer printer;
	String name;
	
	@Resource(name="printer")
	public void setPrinter(Printer printer){
		this.printer = printer;
	}
	
	public void setName(String name){
		this.name= name;
	}
	
	public String sayHello(){
		return "Hello " + this.name;
	}
	
	public void print(){
		this.printer.print(sayHello());
	}

}
