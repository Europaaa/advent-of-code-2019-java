package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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

public class Day7Part2 {
    private static final String INPUT_FILE = "y2018/day7";

    private static final Pattern PATTERN = Pattern.compile("Step (\\w) must be finished before step (\\w) can begin\\.");

    private static final int NUM_WORKER = 5;
    private static final int WAIT_TIME = 60;

    public static long calculate(Map<Character, Set<Character>> relations, int numWorker, int waitTime) {
        Map<Character, Long> prerequisites = new HashMap<>(getPrerequisites(relations));

        Set<Character> startingSteps = getStartingSteps(relations);
        // Steps that can be allocated to a worker
        PriorityQueue<Character> availableSteps = new PriorityQueue<>(startingSteps);
        // Stores seconds for which a step can be completed
        Map<Character, Long> readySteps = new HashMap<>();

        char[] workers = new char[numWorker];
        Arrays.fill(workers, ' ');

        StringBuilder builder = new StringBuilder();

        long second = 0;
        while (!availableSteps.isEmpty() || !readySteps.isEmpty()) {
            // Check if any assigned step is ready
            PriorityQueue<Character> steps = new PriorityQueue<>();

            for (int i = 0; i < workers.length; i++) {
                if (workers[i] == ' ') {
                    continue;
                }

                char step = workers[i];
                if (readySteps.get(step) <= second) {
                    steps.add(step);

                    // Free the worker
                    workers[i] = ' ';
                    readySteps.remove(step);
                }
            }

            // Complete all the ready steps
            steps.forEach(builder::append);

            // Generate available steps based on the steps we just completed
            steps.forEach(step -> {
                // Do nothing if there's no next step
                if (!relations.containsKey(step)) {
                    return;
                }

                for (char nextStep : relations.get(step)) {
                    // Update the prerequisites for the next step
                    prerequisites.put(nextStep, prerequisites.get(nextStep) - 1);

                    // Check if all prerequisites are met
                    if (prerequisites.get(nextStep) > 0) {
                        continue;
                    }
                    availableSteps.add(nextStep);
                }
            });

            // Assign steps to idle workers
            for (int i = 0; i < workers.length; i++) {
                if (workers[i] == ' ' && !availableSteps.isEmpty()) {
                    char step = availableSteps.poll();
                    workers[i] = step;
                    readySteps.put(step, second + (step - 'A' + 1) + waitTime);
                }
            }
            second += 1;
        }
        return second - 1;
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

        long time = calculate(relations, NUM_WORKER, WAIT_TIME);
        System.out.println(time);
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
