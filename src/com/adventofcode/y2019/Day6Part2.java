package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6Part2 {

    private static final String INPUT_FILE = "y2019/day6";

    private static final String START = "YOU";
    private static final String END = "SAN";

    private static int getMinTransfers(Map<String, String> orbiting) {
        Map<String, List<String>> orbitedBy = orbiting.keySet().stream().collect(Collectors.groupingBy(orbiting::get));
        return backtrack(orbiting.get(START), START, orbiting.get(END), orbiting, orbitedBy).get();
    }

    private static Optional<Integer> backtrack(String current, String from, String target, Map<String, String> orbiting, Map<String, List<String>> orbitedBy) {
        if (target.equals(current)) {
            return Optional.of(0);
        }

        Set<String> objects = new HashSet<>();
        if (orbitedBy.containsKey(current)) {
            objects.addAll(orbitedBy.get(current));
        }
        if (orbiting.containsKey(current)) {
            objects.add(orbiting.get(current));
        }
        objects.remove(from);

        Optional<Integer> min = Optional.empty();
        for (String object : objects) {
            Optional<Integer> count = backtrack(object, current, target, orbiting, orbitedBy);
            if (count.isPresent()) {
                min = Optional.of(min.map(m -> Math.min(count.get(), m) + 1).orElse(count.get() + 1));
            }
        }
        return min;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, String> orbiting = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(getMinTransfers(orbiting));
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
