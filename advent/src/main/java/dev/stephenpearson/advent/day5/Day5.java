package dev.stephenpearson.advent.day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day5 {
	
	
	
	
	
    public static void main(String[] args) throws IOException {
    	
        String input = new String(Files.readAllBytes(Paths.get("src/main/resources/dataInput/day5/input.txt")));

        List<String> lines = Arrays.asList(input.split("\n"));
        List<String> rules = new ArrayList<>();
        List<String> updates = new ArrayList<>();
        boolean isRules = true;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                isRules = false;
                continue;
            }
            if (isRules) {
                rules.add(line);
            } else {
                updates.add(line);
            }
        }

        int sumOfMiddlePagesPart1 = correctlyOrderedUpdatesMiddlePageSum(rules, updates);
        System.out.println("Sum of middle page numbers (Part 1): " + sumOfMiddlePagesPart1);

        int sumOfMiddlePagesPart2 = sumMiddlePagesForReorderedUpdates(rules, updates);
        System.out.println("Sum of middle page numbers after reordering (Part 2): " + sumOfMiddlePagesPart2);
    }
    
    
    
    

    private static int correctlyOrderedUpdatesMiddlePageSum(List<String> rules, List<String> updates) {
        Map<Integer, Set<Integer>> rulesMap = buildRulesMap(rules);

        int total = 0;
        for (String update : updates) {
            List<Integer> pages = parsePages(update);

            if (isUpdateGood(pages, rulesMap)) {
                total += getMiddlePage(pages);
            }
        }

        return total;
    }
    
    
    

    private static int sumMiddlePagesForReorderedUpdates(List<String> rules, List<String> updates) {
        Map<Integer, Set<Integer>> rulesMap = buildRulesMap(rules);

        int total = 0;
        for (String update : updates) {
            List<Integer> pages = parsePages(update);

            if (!isUpdateGood(pages, rulesMap)) {
                List<Integer> reordered = reorderPages(pages, rulesMap);
                total += getMiddlePage(reordered);
            }
        }

        return total;
    }

    private static Map<Integer, Set<Integer>> buildRulesMap(List<String> rules) {
    	
        Map<Integer, Set<Integer>> rulesMap = new HashMap<>();
        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            
            
            int before = Integer.parseInt(parts[0].trim());
            int after = Integer.parseInt(parts[1].trim());
            
            rulesMap.computeIfAbsent(after, k -> new HashSet<>()).add(before);
        }
        
        
        return rulesMap;
    }

    private static List<Integer> parsePages(String update) {
    	
        List<Integer> pages = new ArrayList<>();
        for (String p : update.split(",")) {
            pages.add(Integer.parseInt(p.trim()));
        }
        return pages;
    }

    private static boolean isUpdateGood(List<Integer> pages, Map<Integer, Set<Integer>> rulesMap) {
        Set<Integer> pagesInUpdate = new HashSet<>(pages);

        for (int i = 0; i < pages.size(); i++) {
            int currentPage = pages.get(i);

            if (rulesMap.containsKey(currentPage)) {
                for (int mustComeBefore : rulesMap.get(currentPage)) {
                	
                	
                    if (pagesInUpdate.contains(mustComeBefore)) {
                        int indexOfMustComeBefore = pages.indexOf(mustComeBefore);
                        if (indexOfMustComeBefore > i) {
                 
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private static int getMiddlePage(List<Integer> pages) {
        int middleIndex = pages.size() / 2;
        return pages.get(middleIndex);
    }
    
    
    //I think its not such a good attempt here, I think this is rather slow there is most likely a better way but its all I could manage without spending more time
    private static List<Integer> reorderPages(List<Integer> pages, Map<Integer, Set<Integer>> rulesMap) {

        boolean changed;
        
        do {
            changed = false;
                        
            for (int i = 0; i < pages.size(); i++) {
                int currentPage = pages.get(i);
                
                
                if (rulesMap.containsKey(currentPage)) {
                    for (int mustComeBefore : rulesMap.get(currentPage)) {
                        int beforeIndex = pages.indexOf(mustComeBefore);
                        int afterIndex = i; 
                        if (beforeIndex > afterIndex) {
                            pages.remove(beforeIndex);
                            pages.add(afterIndex, mustComeBefore);
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        return pages;
    }
}
