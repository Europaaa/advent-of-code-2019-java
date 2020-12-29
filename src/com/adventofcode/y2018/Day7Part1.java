package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7Part1 {
    private static final String INPUT_FILE = "y2018/day7";

    private static final Pattern PATTERN = Pattern.compile("Step (\\w) must be finished before step (\\w) can begin\\.");

    public static String calculate(Map<Character, Set<Character>> relations) {
        Map<Character, Long> prerequisites = new HashMap<>(getPrerequisites(relations));

        Set<Character> startingSteps = getStartingSteps(relations);
        PriorityQueue<Character> availableSteps = new PriorityQueue<>(startingSteps);

        StringBuilder builder = new StringBuilder();
        while (!availableSteps.isEmpty()) {
            char step = availableSteps.poll();
            builder.append(step);

            if (!relations.containsKey(step)) {
                continue;
            }

            for (char nextStep : relations.get(step)) {
                prerequisites.put(nextStep, prerequisites.get(nextStep) - 1);

                if (prerequisites.get(nextStep) > 0) {
                    continue;
                }
                availableSteps.add(nextStep);
            }
        }
        return builder.toString();
    }

    private static Set<Character> getStartingSteps(Map<Character, Set<Character>> relations) {
        Set<Character> step1s = new HashSet<>(relations.keySet());
        Set<Character> step2s = relations.values().stream().flatMap(Set::stream).collect(Collectors.toSet());

        // Starting steps are those that never appears as step2
        step1s.removeAll(step2s);
        return step1s;
    }

    private static Map<Character, Long> getPrerequisites(Map<Character, Set<Character>> relations) {
        return relations.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<Character, Set<Character>> relations = getInput(CommonUtils.getInputFile(INPUT_FILE));

        String result = calculate(relations);
        System.out.println(result);
    }

    private static Map<Character, Set<Character>> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Map<Character, Set<Character>> relations = new HashMap<>();
            while (scanner.hasNext()) {
                Matcher matcher = PATTERN.matcher(scanner.nextLine());
                if (!matcher.matches()) {
                    continue;
                }

                char step1 = matcher.group(1).charAt(0);
                char step2 = matcher.group(2).charAt(0);

                if (!relations.containsKey(step1)) {
                    relations.put(step1, new HashSet<>());
                }
                relations.get(step1).add(step2);
            }
            return relations;
        }
    }
}
