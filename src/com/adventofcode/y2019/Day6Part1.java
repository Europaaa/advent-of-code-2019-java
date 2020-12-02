package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day6Part1 {

    private static final String INPUT_FILE = "y2019/day6";

    private static int countOrbits(Map<String, String> orbiting) {
        Map<String, Integer> directOrbits = new HashMap<>();
        for (String object : orbiting.keySet()) {
            int count = 0;
            while (orbiting.containsKey(object) && !directOrbits.containsKey(object)) {
                count++;
                object = orbiting.get(object);
            }
            directOrbits.put(object, count + directOrbits.getOrDefault(object, 0));
        }

        return directOrbits.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, String> orbiting = getInput(CommonUtils.getInputFile(INPUT_FILE));
        System.out.println(countOrbits(orbiting));
    }

    private static Map<String, String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Map<String, String> parents = new HashMap<>();
            while (scanner.hasNext()) {
                String[] tokens = scanner.nextLine().split("\\)");
                parents.put(tokens[1], tokens[0]);
            }
            return parents;
        }
    }
}
