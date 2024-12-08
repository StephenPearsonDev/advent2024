package dev.stephenpearson.advent.day4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {

    public static int countPartOne = 0;
    public static int countPartTwo = 0;

    public static void main(String[] args) {
        try (InputStream is = Day4.class.getResourceAsStream("/dataInput/day4/input.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            List<String> rows = new ArrayList<>();
            reader.lines().forEach(rows::add);

            List<String> allLines = getAllDirections(rows);

            Pattern forwardPattern = Pattern.compile("XMAS");
            Pattern backwardPattern = Pattern.compile("SAMX");

            //solution for part 1
            for (String line : allLines) {
                Matcher forwardMatcher = forwardPattern.matcher(line);
                Matcher backwardMatcher = backwardPattern.matcher(line);

                while (forwardMatcher.find()) {
                    countPartOne++;
                }

                while (backwardMatcher.find()) {
                    countPartOne++;
                }
            }
            
            
            //solution for part 2
            for (int row = 1; row < rows.size() - 1; row++) {
                for (int col = 1; col < rows.get(0).length() - 1; col++) {
                    if (isXMas(rows, row, col)) {
                        countPartTwo++;
                    }
                }
            }

            System.out.println("Part One - Total occurrences of XMAS: " + countPartOne);
            System.out.println("Part Two - Total occurrences of X-MAS: " + countPartTwo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    //couldn't figure it out with the regex approach like before so did it manually
    private static boolean isXMas(List<String> rows, int row, int col) {
        char topLeft = rows.get(row - 1).charAt(col - 1);
        char topRight = rows.get(row - 1).charAt(col + 1);
        char middle = rows.get(row).charAt(col);
        char bottomLeft = rows.get(row + 1).charAt(col - 1);
        char bottomRight = rows.get(row + 1).charAt(col + 1);

        return ((topLeft == 'M' && middle == 'A' && bottomRight == 'S') ||
                (topLeft == 'S' && middle == 'A' && bottomRight == 'M')) &&
               ((topRight == 'M' && middle == 'A' && bottomLeft == 'S') ||
                (topRight == 'S' && middle == 'A' && bottomLeft == 'M'));
    }
    
    
    //not very elegant approach. Made new lists of "rows" with all the directions
    private static List<String> getAllDirections(List<String> rows) {
        List<String> allLines = new ArrayList<>();
        int rowCount = rows.size();
        int colCount = rows.get(0).length();

        allLines.addAll(rows);

        for (int col = 0; col < colCount; col++) {
            StringBuilder vertical = new StringBuilder();
            for (int row = 0; row < rowCount; row++) {
                vertical.append(rows.get(row).charAt(col));
            }
            allLines.add(vertical.toString());
        }

        for (int d = -(rowCount - 1); d < colCount; d++) {
            StringBuilder diagonal = new StringBuilder();
            for (int row = 0; row < rowCount; row++) {
                int col = row + d;
                if (col >= 0 && col < colCount) {
                    diagonal.append(rows.get(row).charAt(col));
                }
            }
            allLines.add(diagonal.toString());
        }

        for (int d = 0; d < rowCount + colCount - 1; d++) {
            StringBuilder antiDiagonal = new StringBuilder();
            for (int row = 0; row < rowCount; row++) {
                int col = d - row;
                if (col >= 0 && col < colCount) {
                    antiDiagonal.append(rows.get(row).charAt(col));
                }
            }
            allLines.add(antiDiagonal.toString());
        }

        return allLines;
    }
}
