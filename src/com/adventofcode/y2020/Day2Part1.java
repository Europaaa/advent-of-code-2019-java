package com.adventofcode.y2020;

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

public class Day2Part1 {

    private static final String INPUT_FILE = "y2020/day2";
    private static final Pattern PATTERN = Pattern.compile("(\\d+)-(\\d+)\\s+([a-z]):\\s+([a-z]+)");

    public static long calculate(List<String> lines) {
        long count = 0;
        for (String line: lines) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.matches()) {
                continue;
            }

            long min = Long.parseLong(matcher.group(1));
            long max = Long.parseLong(matcher.group(2));
            char target = matcher.group(3).charAt(0);

            String password = matcher.group(4);

            if (isValid(password, target, min, max)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isValid(String password, char target, long min, long max) {
        char[] chars = password.toCharArray();

        long count = 0;
        for (char c : chars) {
            if (c != target) {
                continue;
            }
            count++;
        }
        return count >= min && count <= max;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> lines = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = calculate(lines);
        System.out.println(result);
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        }
    }
}
