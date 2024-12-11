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
			
			//List<char[]> mapLines = reader.lines().map(String::toCharArray).toList();
//			mapLines.forEach(System.out::println);			
//			
//			Set<Character> uniqueAntenna = new HashSet<>();
//			mapLines.stream().forEach(line -> {
//				for(char c : line) {
//					uniqueAntenna.add(c);
//				}
//			});
//			
//			uniqueAntenna.remove('.');
//			uniqueAntenna.forEach(System.out::println);
//			
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

			
			
			System.out.println("unique antiNodes: " +  uniqueAntiNodeCoords.size());
			
			
			
			
		
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("File IO error");
		}
	}
	
	//do out of bounds checks
	public static boolean isOnBoard(int[] antiNodeCoords, char[][] antennaMap) {
		//check both x and y coords for the antiNode
		return (antiNodeCoords[0] >= 0 && antiNodeCoords[0] < antennaMap.length && antiNodeCoords[1] >= 0 && antiNodeCoords[1] < antennaMap.length);
		
	}
	
	public record AntiNodeCoordinate (int row, int col) {};
	
//	public static int[] findNext(List<int[]> arrs) {
//		
//		for(int i = 0; i < arrs.size(); i++) {
//			
//			
//			for(int t= 0; t < arrs.get(i).length; t++) {
//				
//				int[] antiNodeCoords = new int[2];
//				
//				
//				if(t+1 < arrs.get(i).length) {
//					antiNodeCoords[0] = arrs.get(i)[t+1] - arrs.get(i)[t];
//					antiNodeCoords[0] = arrs.get(i)[t+1] - arrs.get(i)[t];
//					
//				}
//				
//				
//				
//			}
//			
//			
//			
//		}
//		
//		return new int[] {1};
//	}
	//might be easier to find the coords of all the antenas and compare them against each other
	//expand the search in a radial manner - assume antennas dont block or get in the way of each other
//	public static int[][] searchForAntenna(int[][] antennaCoords) {
//		
//		//get instance of an antenna
//
//		
//		
//		
//		
//		return new int[1][1];
//		
//	}

}
