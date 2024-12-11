package dev.stephenpearson.advent.day7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 {

	
	
    private static List<Long> sums = new ArrayList<>();
    private static List<int[]> operands = new ArrayList<>();
    
    

    public static void main(String[] args) {
        try (InputStream is = Day7.class.getClassLoader().getResourceAsStream("dataInput/day7/input.txt")) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String line;

            // Stream this later
            while (null != (line = reader.readLine())) {

                String[] parts = line.split(":");

                // Remember the numbers will have ' ' at the beginning. Trim?
                if (parts.length == 2) {

                    sums.add(Long.parseLong(parts[0]));

                    // Parse straight to numbers instead
                    operands.add(Arrays.stream(parts[1].trim().split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Total sum of all valid target sums - took me ages to figure I was just counting the number of possible true equations!
        long calibrationResult = 0;

        for (int i = 0; i < sums.size(); i++) {
            Long targetSum = sums.get(i);
            int[] currentOperands = operands.get(i);

            // Check if we can achieve the target sum for the current line
            if (findTargetSum(targetSum, currentOperands)) {
                calibrationResult += targetSum;
            }

       
        }
        
        System.out.println("part 1: " + calibrationResult);
        
        
        long calibrationResultPart2 = 0;

        for (int i = 0; i < sums.size(); i++) {
            Long targetSum = sums.get(i);
            int[] currentOperands = operands.get(i);

            if (findNewTargetSum(targetSum, currentOperands)) {
            	calibrationResultPart2 += targetSum;
            }

       
        }
        
        System.out.println("part 1: " + calibrationResultPart2);
        		


        
        

        
        //whats an easy way to pass in a new version of the operands with a sliding window of two operands fused?
        
//        long newCalibrationResult = 0;
//
//        for (int i = 0; i < sums.size(); i++) {
//            Long targetSum = sums.get(i);
//            int[] currentOperands = operands.get(i);
//
//            
//            //this is a poor approach - but its the only way i can start
//            for(int c = 0; c < operands.size(); c++) {
//            	
//            	int[] operandsWithConcat = new int[currentOperands.length-1];
//            	
//            	
//            	
//            	int concatAIndex = c;
//            	int concatBIndex = c+1;
//            	
//            	if(concatBIndex < operandsWithConcat.length) {
//            		
//            		long fused = concatinate(currentOperands[concatAIndex], currentOperands[concatBIndex]);
//            		
//            	}
//            	
//            	for(int t = 0; t < operandsWithConcat.length; i++) {
//            		
//            		
//            		if(t != concatAIndex && t != concatBIndex) {
//            			operandsWithConcat[t] = currentOperands[t];
//            		} else  {
//            			
//            		}
//            		
//            		
//            	}
//            	
//            	
//            	if (findNewTargetSum(targetSum, currentOperands)) {
//                    newCalibrationResult += targetSum;
//                }
//            }
//            
//            
//
//       
//        }

    }

    // Exit early - if current sum becomes larger than target sum, move on
    // Normally we should account for multiplication of 0, which would bring the
    // runningSum down.
    // I manually checked the data and found no 0 values in the operands, so I will
    // just assume the sum can only grow.
    private static boolean findTargetSum(Long targetSum, int[] operands) {

   
        if (operands.length == 1) {
            return operands[0] == targetSum;
        }

        return calculateRecursively(operands, 0, 0, targetSum);
    }
    
    private static boolean findNewTargetSum(Long targetSum, int[] operands) {

    	   
        if (operands.length == 1) {
            return operands[0] == targetSum;
        }
        

        return calculateNewCalibrationResultRecursively(operands, 0, operands[0], targetSum);
    }
    
    
    

    private static boolean calculateRecursively(int[] operands, int index, long currentSum, long targetSum) {

        if (currentSum > targetSum) {
            return false;
        }

        if (index == operands.length) {
            return currentSum == targetSum;
        }

        int currentOperand = operands[index];

        //add
        if (calculateRecursively(operands, index + 1, currentSum + currentOperand, targetSum)) {
            return true;
        }

       //multiply
        if (currentOperand > 0 && currentSum <= targetSum / currentOperand) {
            if (calculateRecursively(operands, index + 1, currentSum * currentOperand, targetSum)) {
                return true;
            }
        }

        return false;
    }
    
    
    //new operand || merges the numbers. First thought was just use a kind of sliding concat window to jump through the operands like before.
    public static boolean calculateNewCalibrationResultRecursively(int[] operands, int index, long currentSum, long targetSum) {
    	
    	if (currentSum > targetSum) {
            return false;
        }

    	 if (index == operands.length - 1) {
             return currentSum == targetSum;
         }
        
        int nextOperand = operands[index + 1];
        
        //add
        if (calculateNewCalibrationResultRecursively(operands, index + 1,  currentSum + nextOperand, targetSum)) {
            return true;
        }

       //multiply
        if (nextOperand > 0 && currentSum <= targetSum / nextOperand) {
            if (calculateNewCalibrationResultRecursively(operands, index + 1, currentSum * nextOperand, targetSum)) {
                return true;
            }
        }
        
        long concatenatedValue = concatinate(currentSum, nextOperand);
        if (calculateNewCalibrationResultRecursively(operands, index + 1, concatenatedValue, targetSum)) {
            return true;
        }

    	
    	return false;
    }
    
    
    
    //cant remember how to do it mathematically, I remember involves multiplication using 10
    public static long concatinate(long a, long b) {
    	return Long.parseLong(String.valueOf(a) + String.valueOf(b));
    }
}
