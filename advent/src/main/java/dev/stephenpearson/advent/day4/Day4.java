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

    public static int count = 0;

    public static void main(String[] args) {
        try (InputStream is = Day4.class.getResourceAsStream("/dataInput/day4/input.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            List<String> rows = new ArrayList<>();
            reader.lines().forEach(rows::add);

            List<String> allLines = getAllDirections(rows);

            Pattern forwardPattern = Pattern.compile("XMAS");
            Pattern backwardPattern = Pattern.compile("SAMX");

            for (String line : allLines) {
                Matcher forwardMatcher = forwardPattern.matcher(line);
                Matcher backwardMatcher = backwardPattern.matcher(line);

                while (forwardMatcher.find()) {
                    count++;
                }

                while (backwardMatcher.find()) {
                    count++;
                }
            }

            System.out.println("Total occurrences of XMAS: " + count);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
