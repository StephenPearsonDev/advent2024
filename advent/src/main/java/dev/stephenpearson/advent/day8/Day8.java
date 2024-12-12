package dev.stephenpearson.advent.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Day8 {
	
	public static void main(String[] args) {
		
		
		
		try(InputStream is = Day8.class.getClassLoader().getResourceAsStream("dataInput/day8/input.txt")) {
			
			if(is == null) {
				
				throw new IOException("File not found - check the path");
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			
		
			char[][] antennaMap = reader.lines().map(String::toCharArray).toArray(char[][]::new);
			
			Map<Character, List<int[]>> coordMap = new HashMap<>();
			
			IntStream.range(0, antennaMap.length).forEach(row ->
				IntStream.range(0, antennaMap[row].length)
					//filter(antennaMap[row][col] != '.')
					.forEach(col -> {
					char c = antennaMap[row][col];
					
					if(c!= '.') {
						coordMap.computeIfAbsent(c, k -> new ArrayList<>())
						.add(new int[] {row,col});
					}
					
				})
			);
			
			
			coordMap.entrySet().forEach(entry ->{
				System.out.println(entry.getKey());
				entry.getValue().forEach(arr ->{
					Arrays.stream(arr).forEach(num -> System.out.print(num + " "));
					System.out.println();
				});
			});
			
			//took me ages to figure out the problem - the contents of the array werent being checked so used a record instead. Was getting too many duplicates.
			//Set<int[]> uniqueAntiNodesCoords = new HashSet<>();
			
			Set<AntiNodeCoordinate> uniqueAntiNodeCoords = getUniqueAntiNodeCoordinates(coordMap, antennaMap);
           
			
			Set<AntiNodeCoordinate> extendedUniqueAntiNodeCoords = getExtendedUniqueAntiNodeCoordinates(coordMap, antennaMap);
			

			
			System.out.println("unique antiNodes: " +  uniqueAntiNodeCoords.size());
			System.out.println("extended unique antiNodes: " +  extendedUniqueAntiNodeCoords.size());
			
			
			debugAndPrintTest(getExtendedUniqueAntiNodeCoordinates(coordMap, antennaMap), antennaMap);

		
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("File IO error");
		}
	}
	
	
	private static Set<AntiNodeCoordinate> getUniqueAntiNodeCoordinates(Map<Character, List<int[]>> coordMap, char[][] antennaMap) {
		Set<AntiNodeCoordinate> uniqueAntiNodeCoords = new HashSet<>();
		
		for(Map.Entry<Character, List<int[]>> entry : coordMap.entrySet()) {
			
			List<int[]> positions  = entry.getValue();
			
			for(int i = 0; i < positions.size(); i++) {
				
				for(int j = i+1; j < positions.size(); j++) {
					
					
					//build pairs of positions to calculate the antinodes
					int[] p1 = positions.get(i);
					int[] p2 = positions.get(j);
					
					//figure out the direction or which way its pointing
					int dx = p2[0] - p1[0];
					int dy = p2[1] - p1[1];

					int[] antiNode1xy = {p1[0] - dx, p1[1] - dy}; 
					int[] antiNode2xy = {p2[0] + dx, p2[1] + dy};
					
					
					
					//need to make sure these nodes are within the board still
					if(isOnBoard(antiNode1xy,antennaMap)) {
						uniqueAntiNodeCoords.add(new AntiNodeCoordinate(antiNode1xy[0], antiNode1xy[1]));
						
					}
					
					if(isOnBoard(antiNode2xy, antennaMap)) {
						uniqueAntiNodeCoords.add(new AntiNodeCoordinate(antiNode2xy[0], antiNode2xy[1]));
					}

				}
				
			}
			
		}
		
		
		
		return uniqueAntiNodeCoords;
	}
	
	
	//just go along the vector until out of bounds? first brute force and calculate all positions, later optimize if time - memoize previously calculated positions?
	private static Set<AntiNodeCoordinate> getExtendedUniqueAntiNodeCoordinates(Map<Character, List<int[]>> coordMap, char[][] antennaMap) {

	    Set<AntiNodeCoordinate> extendedUniqueAntiNodeCoords = new HashSet<>();

	    for (Map.Entry<Character, List<int[]>> entry : coordMap.entrySet()) {

	        List<int[]> positions = entry.getValue();

	        for (int i = 0; i < positions.size(); i++) {

	            for (int j = i + 1; j < positions.size(); j++) {

	                int[] p1 = positions.get(i);
	                int[] p2 = positions.get(j);

	                int dx = p2[0] - p1[0];
	                int dy = p2[1] - p1[1];

	                // Add the positions of p1 and p2 
	                extendedUniqueAntiNodeCoords.add(new AntiNodeCoordinate(p1[0], p1[1]));
	                extendedUniqueAntiNodeCoords.add(new AntiNodeCoordinate(p2[0], p2[1]));

	                // Extend in both directions
	                int[] antiNode1xy = {p1[0] - dx, p1[1] - dy};
	                int[] antiNode2xy = {p2[0] + dx, p2[1] + dy};

	                while (isOnBoardNew(antiNode1xy, antennaMap)) {
	                    extendedUniqueAntiNodeCoords.add(new AntiNodeCoordinate(antiNode1xy[0], antiNode1xy[1]));
	                    antiNode1xy[0] -= dx;
	                    antiNode1xy[1] -= dy;
	                }

	                while (isOnBoardNew(antiNode2xy, antennaMap)) {
	                    extendedUniqueAntiNodeCoords.add(new AntiNodeCoordinate(antiNode2xy[0], antiNode2xy[1]));
	                    antiNode2xy[0] += dx;
	                    antiNode2xy[1] += dy;
	                }
	            }
	        }
	    }

	    return extendedUniqueAntiNodeCoords;
	}

	
	//do out of bounds checks
		private static boolean isOnBoard(int[] antiNodeCoords, char[][] antennaMap) {
			
			System.out.println("looking at coords: x - " + antiNodeCoords[0] +  " y - " + antiNodeCoords[1]);
			//check both x and y coords for the antiNode
			return (antiNodeCoords[0] >= 0 && antiNodeCoords[0] < antennaMap.length && antiNodeCoords[1] >= 0 && antiNodeCoords[1] < antennaMap.length);
			
		}
		
		private static boolean isOnBoardNew(int[] antiNodeCoords, char[][] antennaMap) {
			
			System.out.println("looking at coords: x: " + antiNodeCoords[0] +  " y: " + antiNodeCoords[1]);
			//check both x and y coords for the antiNode
			
			if(antiNodeCoords[0] >= 0 && antiNodeCoords[0] < antennaMap.length && antiNodeCoords[1] >= 0 && antiNodeCoords[1] < antennaMap[0].length) {
				System.out.println("returning true for: " +  antiNodeCoords[0] + " and " + antiNodeCoords[1]);
				return true;
			} else {
				return false;
			}
			
			
		}
	
	private record AntiNodeCoordinate (int row, int col) {};
	
	//got stuck, need to print and see whats going on
	private static void debugAndPrintTest(Set<AntiNodeCoordinate> antiNodes, char[][] antennaMap) {
		
		System.out.printf("there are %d nodes in the set\n", antiNodes.size());
		System.out.println();
		System.out.println("map before: ");
		
		Arrays.stream(antennaMap).forEach(System.out::println);
		
		
		System.out.println();
		System.out.println("map after: ");
		int totalAntiNodes = antiNodes.size();
		int overlappingNodes = 0;
		
		
		for(AntiNodeCoordinate coord : antiNodes) {
			char current = antennaMap[coord.row][coord.col];
			antennaMap[coord.row][coord.col] = current == '.' ? '#' : current;
			if (current != '.') overlappingNodes++;

		}
	
		Arrays.stream(antennaMap).forEach(System.out::println);
		System.out.println("Total anti nodes: " + totalAntiNodes);
		System.out.println("Overlapping anti nodes: " + overlappingNodes);

		
	}
	
	

}
