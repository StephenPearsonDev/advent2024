package dev.stephenpearson.advent.day3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
	
	

	
	public static void main(String[] args) throws IOException {
		
		
		//Probably wouln't be a good idea if the file were huge - rather use inputstream - go over the individual chars
		String input = new String(Files.readAllBytes(Paths.get("src/main/resources/dataInput/day3/input.txt")));
		
		 
		 Pattern pattern = Pattern.compile("mul\\((\\d{1,10}),(\\d{1,10})\\)");
			Matcher matcher = pattern.matcher(input);
		 
		 long sum = 0;
			
			while(matcher.find()) {
				
				
				int x = Integer.parseInt(matcher.group(1));
				int y = Integer.parseInt(matcher.group(2));
				
				sum += (long) x * y;
			}
		 
		System.out.println(sum);
		
	
		
		
	}
	
}