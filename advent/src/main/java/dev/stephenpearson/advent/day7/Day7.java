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

        // Total sum of all valid target sums - took me ages to figure I was just counting the number of possible true equations!
        long calibrationResult = 0;

        for (int i = 0; i < sums.size(); i++) {
            Long targetSum = sums.get(i);
            int[] currentOperands = operands.get(i);

            // Check if we can achieve the target sum for the current line
            if (findTargetSum(targetSum, currentOperands)) {
                calibrationResult += targetSum;
            }

       
        }


//        System.out.println(sums.size());

        // Print the total calibration result
        System.out.println("Possible true sums: " + calibrationResult);
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

    private static boolean calculateRecursively(int[] operands, int index, long currentSum, long targetSum) {

//
//        System.out.println("Index: " + index + ", CurrentSum: " + currentSum + ", TargetSum: " + targetSum);

 
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
}
