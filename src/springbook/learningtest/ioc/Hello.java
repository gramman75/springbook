package springbook.learningtest.ioc;

public class Hello {

	Printer printer;
	String name;
	
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
