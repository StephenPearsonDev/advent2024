package dev.stephenpearson.advent.day9;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day9 {
	
	
	
	
    
    public static void main(String[] args) {
    	
    	
        try {
            Path input = Paths.get(Day9.class.getClassLoader().getResource("dataInput/day9/input.txt").toURI());    
            String content = Files.readString(input).trim();
            int[] digits = content.chars().map(c -> c - '0').toArray();
            
            List<Integer> disk = new ArrayList<>();
            
            int fileId = 0;
            
            //need to process the string in pairs one for the block/  file and one for the freespace 
            //build the disk like in the example so it has the free space between the blocks
            for(int i = 0; i < digits.length; i += 2) {
                int fileLength = digits[i];
                if(fileLength > 0) {
                    for(int j = 0; j < fileLength; j++) {
                        disk.add(fileId);
                    }
                    fileId++;
                }
                if(i + 1 < digits.length) {
                    int freeLength = digits[i + 1];
                    for(int j = 0; j < freeLength; j++) {
                    	
                    	//in example it was '.' but i just add -1 because i know the data doesnt have it
                        disk.add(-1);
                    }
                }
            }
            
            
            // Compact the disk, move stuff on right to left
            while(true) {
                int freePos = disk.indexOf(-1);
                if(freePos == -1) {
                    break; // No free space left
                }
                
                int movePos = -1;
                for(int i = disk.size() - 1; i > freePos; i--) {
                    if(disk.get(i) != -1) {
                        movePos = i;
                        break;
                    }
                }
                
                if(movePos == -1) {
                    break; 
                }
                
                // Move block over on the left
                disk.set(freePos, disk.get(movePos));
                disk.set(movePos, -1);
            }
            
            
           
            // new checksum
            long checksum = 0;
            
            for(int i = 0; i < disk.size(); i++) {
                int id = disk.get(i);
                if(id != -1) {
                    checksum += (long)i * id;
                }
            }
            System.out.println(checksum);
            
        } catch (IOException | URISyntaxException e) {
        	
            e.printStackTrace();
        }
        
    }
    
    
    
    
    
    
    
}
