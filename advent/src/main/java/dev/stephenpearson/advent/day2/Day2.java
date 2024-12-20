package dev.stephenpearson.advent.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day2 {

    private static final String INPUT_FILE = "dataInput/day2/input.txt";
    private static final int MIN_DIFFERENCE = 1;
    private static final int MAX_DIFFERENCE = 3;
    private static int safeReports;
    private static int safeDampenerReports;

    public static void main(String[] args) {
        try (InputStream is = Day2.class.getClassLoader().getResourceAsStream(INPUT_FILE)) {
            if (is == null) {
                System.out.println("File not found: " + INPUT_FILE);
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            List<int[]> numberArrays = reader.lines()
                    .map(line -> Arrays.stream(line.trim().split("\\s+"))
                            .mapToInt(Integer::parseInt)
                            .toArray())
                    .collect(Collectors.toList());

            for (int[] arr : numberArrays) {
                if (isSafe(arr)) {
                    safeReports++;
                }
                if (isSafeWithDampener(arr)) {
                    safeDampenerReports++;
                }
            }

            System.out.printf("%d reports are safe", safeReports);
            System.out.println();
            System.out.printf("%d reports are safe with dampener", safeDampenerReports);

        } catch (IOException e) {
            System.out.println("There was an error reading the data: " + e);
        }
    }

    private static boolean isSafe(int[] levels) {
        if (levels.length < 2) {
            return false;
        }

        boolean isIncreasing = levels[1] > levels[0];

        for (int i = 1; i < levels.length; i++) {
            int difference = levels[i] - levels[i - 1];
            if (Math.abs(difference) < MIN_DIFFERENCE || Math.abs(difference) > MAX_DIFFERENCE) {
                return false;
            }
            if ((difference > 0 && !isIncreasing) || (difference < 0 && isIncreasing)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSafeWithDampener(int[] levels) {
        if (isSafe(levels)) {
            return true;
        }

        for (int i = 0; i < levels.length; i++) {
            int[] modifiedLevels = removeElementAtIndex(levels, i);
            if (isSafe(modifiedLevels)) {
                return true;
            }
        }
        return false;
    }

    private static int[] removeElementAtIndex(int[] arr, int index) {
        return IntStream.range(0, arr.length)
                .filter(i -> i != index)
                .map(i -> arr[i])
                .toArray();
    }
}
