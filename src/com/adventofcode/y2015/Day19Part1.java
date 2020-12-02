package com.adventofcode.y2015;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19Part1 {

    private static final String INPUT_FILE = "y2015/day19";

    private static final Pattern PATTERN = Pattern.compile("(\\w+) => (\\w+)");

    private static long evaluate(String input, List<String[]> replacements) {
        return evaluate(input, 0, replacements, new HashSet<>());
    }

    private static long evaluate(String input, int i, List<String[]> replacements, Set<String> targets) {
        String substring = input.substring(i);

        long counter = 0;
        for (String[] replacement : replacements) {
            String key = replacement[0];
            String value = replacement[1];

            if (!substring.startsWith(key)) {
                continue;
            }

            String target = input.substring(0, i) + value + input.substring(i + key.length());
            if (targets.add(target)) {
                counter++;
            }
        }
        if (i + 1 < input.length()) {
            return counter + evaluate(input, i + 1, replacements, targets);
        }
        return counter;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Input input = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(input.input, input.replacements));
    }

    private static Input getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Input input = new Input();
            input.replacements = new ArrayList<>();

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                Matcher matcher = PATTERN.matcher(line);
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    input.replacements.add(new String[]{key, value});
                } else {
                    input.input = line;
                }
            }
            return input;
        }
    }

    private static class Input {
        private String input;
        private List<String[]> replacements;
    }
}
