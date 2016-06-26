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
	
	public Integer lineReadTemplate(String filePath, LineCallback callback, int initVal) throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(filePath));
			
			Integer res = initVal;
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
		LineCallback callback = new LineCallback(){
			public Integer doSomethingWithLine(String line, Integer value) throws IOException{
				return value + Integer.valueOf(line);
			}
		};
		
		return lineReadTemplate(filePath, callback, 0);
	}
	
	public Integer calcSum(String filePath) throws IOException{
		BufferedReaderCallback callback = new BufferedReaderCallback(){
			public Integer doSomethingWithReader(BufferedReader br) throws IOException{
				Integer sum=0;
				String line = null;
				
				while((line = br.readLine()) != null){
					sum += Integer.valueOf(line);
				}
				
				return sum;
			}
		};
		
		return fileReadTemplate(filePath, callback);
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
