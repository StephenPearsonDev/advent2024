package dev.stephenpearson.advent.day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
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

            
//            if (guardCoords[0] == -1 || guardCoords[1] == -1) {
//                System.out.println("No guard found in the map!");
//                return;
//            }
            
            
            //dont want to change original array so i can use it for second one
            //Part 2 below
            int distinctPositions = travel(guardCoords.clone());

            System.out.println("Distinct positions visited: " + distinctPositions);

            int loopPositions = 0;

            for (int row = 0; row < map.length; row++) {
                for (int col = 0; col < map[row].length; col++) {
                    if (map[row][col] == '.' && !(row == guardCoords[0] && col == guardCoords[1])) {
                    	
                    	//Brute force placing and removing and checking if a cycle appears.
                    	//It's very slow on my computer - 5 seconds. Must be a faster way I imagine I could use a
                    	//linked list or graph to try to find a cycle quicker but I spent too much time already
                        placeObstacle(new int[] { row, col });
                        if (checkIfCyclic(guardCoords.clone())) {
                            loopPositions++;
                        }
                        removeObstacle(new int[] { row, col });
                    }
                }
            }

            System.out.println("Possible obstruction positions: " + loopPositions);

        } catch (IOException e) {
            System.out.println("error reading file");
            e.printStackTrace();
        }

    }

    public static int travel(int[] guardCoords) {
        Set<String> visited = new HashSet<>();
        visited.add(guardCoords[0] + "," + guardCoords[1]);

        while (guardOnMap) {
            int[] nextPos = nextPosition(guardCoords, currentDirection);
            if (!isOnMap(nextPos)) {
                guardOnMap = false;
                break;
            }
            if (map[nextPos[0]][nextPos[1]] == '#' || map[nextPos[0]][nextPos[1]] == 'O') {
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
        int row = coords[0];
        int col = coords[1];
        switch (dir) {
            case NORTH:
                return new int[] { row - 1, col };
            case EAST:
                return new int[] { row, col + 1 };
            case SOUTH:
                return new int[] { row + 1, col };
            case WEST:
                return new int[] { row, col - 1 };
        }
        throw new IllegalArgumentException();
    }
    
    

    private static boolean isOnMap(int[] pos) {
        int row = pos[0];
        int col = pos[1];
        return row >= 0 && row < map.length && col >= 0 && col < map[0].length;
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
        }
        throw new IllegalArgumentException();
    }

    public enum Directions {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private static boolean checkIfCyclic(int[] startCoords) {
        Set<String> states = new HashSet<>();
        Directions dir = Directions.NORTH;

        int[] coords = startCoords.clone();

        int limit = map.length * map[0].length * 4;
        int steps = 0;

        while (steps < limit) {
            String state = coords[0] + "," + coords[1] + "," + dir;
            if (states.contains(state)) {
                return true;
            }

            states.add(state);
            int[] nextPos = nextPosition(coords, dir);

            if (!isOnMap(nextPos)) {
                return false;
            }
            if (map[nextPos[0]][nextPos[1]] == '#' || map[nextPos[0]][nextPos[1]] == 'O') {
                dir = rotate(dir);
            } else {
                coords = nextPos;
            }

            steps++;
        }
        return false;
    }
    
    
    

    private static void placeObstacle(int[] obstacleCoords) {

        map[obstacleCoords[0]][obstacleCoords[1]] = 'O';

    }
    

    private static void removeObstacle(int[] obstacleCoords) {

        map[obstacleCoords[0]][obstacleCoords[1]] = '.';

    }

}
