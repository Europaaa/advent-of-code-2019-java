package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day5Part1 {

    private static final String INPUT_FILE = "y2015/day5";

    private static final List<String> BOMBS = Arrays.asList("ab", "cd", "pq", "xy");

    private static long evaluate(List<String> codes) {
        return codes.stream().filter(Day5Part1::isNice).count();
    }

    private static boolean isNice(String code) {
        return hasVowels(code, 3) && hasRepeats(code) && !hasBombs(code);
    }

    private static boolean hasVowels(String code, long target) {
        return target <= code.toLowerCase().chars().filter(c -> c == 'a' || c == 'e' || c == 'i' || c == 'o' | c == 'u').count();
    }

    private static boolean hasRepeats(String code) {
        char[] chars = code.toCharArray();
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == chars[i - 1]) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasBombs(String code) {
        return BOMBS.stream().filter(code::contains).findAny().map(c -> true).orElse(false);
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> codes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(codes));
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> inputs = new ArrayList<>();
            while (scanner.hasNext()) {
                inputs.add(scanner.nextLine());
            }
            return inputs;
        }
    }
}
