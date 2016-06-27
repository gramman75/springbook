package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public Integer fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException{
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new FileReader(filePath));
			int ret = callback.doSomethingWithReader(br);
			return ret;
		} catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		} finally{
			if (br != null){
				try{
					br.close();
				} catch(IOException e){
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(filePath));
			
			T res = initVal;
			String line = null;
			
			while((line = br.readLine()) != null){
				res = callback.doSomethingWithLine(line, res);
			}
			
			return res;
		} catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		} finally{
			if (br != null){
				try{
					br.close();
				} catch(IOException e){
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public String concatenate(String filePath) throws IOException{
		LineCallback<String> concatenateCallback = new LineCallback<String>(){
			public String doSomethingWithLine(String line, String value){
				return value + line;
			}
		};
		
		return lineReadTemplate(filePath, concatenateCallback, "");
	}
	
	public Integer calcMultiply(String filePath) throws IOException{
//		BufferedReaderCallback callback = new BufferedReaderCallback(){
//			public Integer doSomethingWithReader(BufferedReader br) throws IOException{
//				Integer multiply = 1;
//				String line = null;
//				
//				while((line= br.readLine()) != null){
//					multiply *= Integer.valueOf(line);
//				}
//				
//				return multiply;
//			}
//			
//		};
//		
//		return fileReadTemplate(filePath, callback);
		LineCallback<Integer> callback = new LineCallback<Integer>(){
			public Integer doSomethingWithLine(String line, Integer value) throws IOException{
				return value * Integer.valueOf(line);
			}
		};
		
		return lineReadTemplate(filePath, callback, 1);
	}
	
	public Integer calcSum(String filePath) throws IOException{
		LineCallback<Integer> callback = new LineCallback<Integer>(){
			public Integer doSomethingWithLine(String line, Integer value) throws IOException{
				return value + Integer.valueOf(line);
			}
		};
		
		return lineReadTemplate(filePath, callback, 0);
//		BufferedReaderCallback callback = new BufferedReaderCallback(){
//			public Integer doSomethingWithReader(BufferedReader br) throws IOException{
//				Integer sum=0;
//				String line = null;
//				
//				while((line = br.readLine()) != null){
//					sum += Integer.valueOf(line);
//				}
//				
//				return sum;
//			}
//		};
//		
//		return fileReadTemplate(filePath, callback);
	}
	
//	public Integer calcSum(String filePath) throws IOException{
//		BufferedReader br = null;
//		try{
//			br = new BufferedReader(new FileReader(filePath));
//			
//			Integer sum = 0;
//			String line = null;
//			
//			while((line = br.readLine()) != null){
//				sum += Integer.valueOf(line);
//			}
//			
//			return sum;
//			
//		} catch(IOException e){
//			System.out.println(e.getMessage());
//			throw e;
//		} finally{
//			if (br != null){
//				try{
//					br.close();
//				} catch(IOException e){
//					System.out.println(e.getMessage());
//				}
//			}
//		}
//	}
}
