package dev.stephenpearson.advent.day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Day6 {

    private static char[][] map;
    private static Directions currentDirection = Directions.NORTH;
    private static boolean guardOnMap = true;

    public static void main(String[] args) {

        try (InputStream is = Day6.class.getClassLoader().getResourceAsStream("dataInput/day6/input.txt")) {
            if (is == null) {
                System.out.println("file not found");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            map = reader.lines().map(String::toCharArray).toArray(char[][]::new);

            int[] guardCoords = new int[] { -1, -1 };

     
            IntStream.range(0, map.length).forEach(row -> {
                int colIndex = IntStream.range(0, map[row].length)
                        .filter(col -> map[row][col] == '^')
                        .findFirst()
                        .orElse(-1);
                if (colIndex != -1) {
                    guardCoords[0] = row;
                    guardCoords[1] = colIndex;
                }
            });

    
            if (guardCoords[0] == -1 || guardCoords[1] == -1) {
                System.out.println("No guard found in the map!");
                return;
            }

            int distinctPositions = travel(guardCoords);
            System.out.println("Distinct positions visited: " + distinctPositions);

        } catch (IOException e) {
            System.out.println("error reading file");
            e.printStackTrace();
        }

    }

    public static int travel(int[] guardCoords) {
    	
    	//distinct positions visited on the map
        Set<String> visited = new HashSet<>();
        
        visited.add(guardCoords[0] + "," + guardCoords[1]);

 
        while (guardOnMap) {

  
            int[] nextPos = nextPosition(guardCoords, currentDirection);

 
            if (!isOnMap(nextPos)) {
                guardOnMap = false;
                break;
            }

        
            if (map[nextPos[0]][nextPos[1]] == '#') {
                currentDirection = rotate(currentDirection);
                continue; 
            }

         
            guardCoords[0] = nextPos[0];
            guardCoords[1] = nextPos[1];
            visited.add(guardCoords[0] + "," + guardCoords[1]);
        }

        return visited.size();
    }


    private static int[] nextPosition(int[] coords, Directions dir) {
        int r = coords[0];
        int c = coords[1];

        switch (dir) {
            case NORTH:
                return new int[] { r - 1, c };
            case EAST:
                return new int[] { r, c + 1 };
            case SOUTH:
                return new int[] { r + 1, c };
            case WEST:
                return new int[] { r, c - 1 };
            default:
                throw new IllegalArgumentException("Unknown direction: " + dir);
        }
    }


    private static boolean isOnMap(int[] pos) {
        int r = pos[0];
        int c = pos[1];
        return r >= 0 && r < map.length && c >= 0 && c < map[0].length;
    }


    public static Directions rotate(Directions direction) {
        switch (direction) {
            case NORTH:
                return Directions.EAST;
            case EAST:
                return Directions.SOUTH;
            case SOUTH:
                return Directions.WEST;
            case WEST:
                return Directions.NORTH;
            default:
                throw new IllegalArgumentException("Unexpected direction: " + direction);
        }
    }

    public enum Directions {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

}
