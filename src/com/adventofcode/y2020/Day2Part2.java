package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2Part2 {

    private static final String INPUT_FILE = "y2020/day2";
    private static final Pattern PATTERN = Pattern.compile("(\\d+)-(\\d+)\\s+([a-z]):\\s+([a-z]+)");

    public static long calculate(List<String> lines) {
        long count = 0;
        for (String line: lines) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.matches()) {
                continue;
            }

            int p1 = Integer.parseInt(matcher.group(1));
            int p2 = Integer.parseInt(matcher.group(2));
            char target = matcher.group(3).charAt(0);

            String password = matcher.group(4);

            if (isValid(password, target, p1 - 1, p2 - 1)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isValid(String password, char target, int p1, int p2) {
        char[] chars = password.toCharArray();
        return p1 < chars.length && p2 < chars.length && (chars[p1] == target ^ chars[p2] == target);
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
