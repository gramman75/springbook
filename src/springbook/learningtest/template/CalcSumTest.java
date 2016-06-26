package springbook.learningtest.template;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalcSumTest {
	Calculator calculator;
	String filePath;
	
	@Before
	public void setup(){
		this.calculator = new Calculator();
		this.filePath = getClass().getResource("numbers.txt").getPath();

	}

	@Test
	public void sumOfNumbers() throws IOException{
		
		int sum = calculator.calcSum(this.filePath);
		System.out.println(sum);
		assertThat(sum,is(15));
	}
	
	@Test
	public void multiplyOfNumbers() throws IOException{
		int multiply = calculator.calcMultiply(this.filePath);
		assertThat(multiply,is(120));
	}

}
