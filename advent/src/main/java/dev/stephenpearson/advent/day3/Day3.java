package dev.stephenpearson.advent.day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
	
	private static boolean doMul = true;

	
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
		 
		System.out.println("part 1 sum: " + sum);
		
		System.out.println("part 2 sum is: " + solvePartTwo(input));
		
		
	}
	
	private static long solvePartTwo(String input) {
      
        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,10}),(\\d{1,10})\\)");
        Pattern doPattern = Pattern.compile("do\\(\\)");
        Pattern dontPattern = Pattern.compile("don'?t\\(\\)");
        
        //not clean but want to store instructions quickly so using Object
        List<Object[]> instructions = new ArrayList<>();


        Matcher mulMatcher = mulPattern.matcher(input);
        while (mulMatcher.find()) {
            int start = mulMatcher.start();
            int x = Integer.parseInt(mulMatcher.group(1));
            int y = Integer.parseInt(mulMatcher.group(2));
          
            instructions.add(new Object[]{"mul", start, x, y});
        }

   
        Matcher doMatcher = doPattern.matcher(input);
        while (doMatcher.find()) {
            int start = doMatcher.start();
            instructions.add(new Object[]{"do", start});
        }

        Matcher dontMatcher = dontPattern.matcher(input);
        while (dontMatcher.find()) {
            int start = dontMatcher.start();
            instructions.add(new Object[]{"dont", start});
        }

        //make sure the instructions in same order as input data given using the indexes set
        instructions.sort(Comparator.comparingInt(instruction -> (Integer) instruction[1]));

        boolean enabled = true;
        long sum = 0;
        
        //compute the instructions in order
        for (Object[] instruction : instructions) {
            String type = (String) instruction[0];
            switch (type) {
                case "do":
                    enabled = true;
                    break;
                case "dont":
                    enabled = false;
                    break;
                case "mul":
                    if (enabled) {
                        int x = (Integer) instruction[2];
                        int y = (Integer) instruction[3];
                        sum += (long) x * y;
                    }
                    break;
            }
        }

        return sum;
    }

	
}