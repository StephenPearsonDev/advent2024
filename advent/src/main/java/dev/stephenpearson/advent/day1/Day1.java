package dev.stephenpearson.advent.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1 {
    
    private static final String INPUT_FILE = "dataInput/day1/input.txt";

    public static void main(String[] args) {
        
        try (InputStream is = Day1.class.getClassLoader().getResourceAsStream(INPUT_FILE)) {
            
            if (is == null) {
                System.err.println("File not found: " + INPUT_FILE);
                return;
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            
            Stream<String> lines = reader.lines();
            
            PriorityQueue<Integer> minHeapA = new PriorityQueue<>();
            PriorityQueue<Integer> minHeapB = new PriorityQueue<>();
            
            List<Integer> listA = new ArrayList<>();
            List<Integer> listB = new ArrayList<>();
            
        
            
            
            lines.forEach(line -> {
                String[] numbers = line.trim().split("\\s+");
                
                if (numbers.length == 2) {
                    try {
                    	
                    	//process the input and put the left numbers into minHeapA and listA and so on for minHeapB / listB
                    	int left = Integer.parseInt(numbers[0]);
                    	int right = Integer.parseInt(numbers[1]);
                        minHeapA.add(left);
                        minHeapB.add(right);
                        listA.add(left);
                        listB.add(right);
                        
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing number in line: \"" + line + "\". " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid line format: \"" + line + "\". Expected exactly 2 numbers.");
                }
            });

            
            //final outputs
            System.out.println(findDistance(minHeapA, minHeapB));
            System.out.println(computeSimilarityScore(listA, listB));
            
        } catch (IOException e) {
            System.err.println("Error reading resource: " + e.getMessage());
        }
    }
    
    
    //first task
    public static int findDistance(PriorityQueue<Integer> minHeapA, PriorityQueue<Integer> minHeapB) {
    	
    	int runningTotal = 0;
			
		while(!minHeapA.isEmpty() || !minHeapB.isEmpty()) {
			
			int left = minHeapA.poll();
			
			int right = minHeapB.poll();
			
			if(left >= right) {
				runningTotal += left - right;
			} else {
				runningTotal += right - left;
			}
			
		}
		
	        
        return runningTotal;
    }
    
    
    
    
    
    public static int computeSimilarityScore(List<Integer> listA, List<Integer> listB) {

    	
    	Map<Integer, Long> freqMap = listB.stream()
    			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
    	  int score = listA.stream()
    	            .mapToInt(num -> num * freqMap.getOrDefault(num, 0L).intValue())
    	            .sum();
    	
    	return score;
	}

}

	
