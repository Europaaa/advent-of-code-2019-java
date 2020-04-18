package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16Part2 {

    private static final String INPUT_FILE = "y2015/day16";

    private static final Pattern PATTERN = Pattern.compile("(\\w+): (\\d+)");
    private static final List<String> TARGET_GT = Arrays.asList("cats", "trees");
    private static final List<String> TARGET_LT = Arrays.asList("pomeranians", "goldfish");

    private static final int NUM_AUNTS = 500;

    private static int evaluate(Map<String, Integer>[] aunts, Map<String, Integer> targets) {
        for (int i = 1; i <= NUM_AUNTS; i++) {
            if (matchesTarget(aunts[i - 1], targets)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean matchesTarget(Map<String, Integer> aunt, Map<String, Integer> targets) {
        return aunt.keySet().stream().allMatch(key -> {
            if (TARGET_GT.contains(key)) {
                return aunt.get(key) > targets.get(key);
            }
            if (TARGET_LT.contains(key)) {
                return aunt.get(key) < targets.get(key);

            }
            return targets.get(key).equals(aunt.get(key));
        });
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Integer>[] aunts = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Map<String, Integer> targets = new HashMap<>();
        targets.put("children", 3);
        targets.put("cats", 7);
        targets.put("samoyeds", 2);
        targets.put("pomeranians", 3);
        targets.put("akitas", 0);
        targets.put("vizslas", 0);
        targets.put("goldfish", 5);
        targets.put("trees", 3);
        targets.put("cars", 2);
        targets.put("perfumes", 1);

        System.out.println(evaluate(aunts, targets));
    }

    private static Map<String, Integer>[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Map<String, Integer>[] aunts = new HashMap[NUM_AUNTS];
            for (int i = 0; i < NUM_AUNTS; i++) {
                Matcher matcher = PATTERN.matcher(scanner.nextLine());

                Map<String, Integer> counters = new HashMap<>();
                while (matcher.find()) {
                    counters.put(matcher.group(1), Integer.valueOf(matcher.group(2)));
                }

                aunts[i] = counters;
            }
            return aunts;
        }
    }
}
