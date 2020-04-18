package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day10Part2 {

    private static final String INPUT_FILE = "y2015/day10";

    private static final int N_REPEAT = 50;

    public static String evaluate(long seed, int repeats) {
        String number = String.valueOf(seed);
        for (int i = 0; i < repeats; i++) {
            number = evaluate(number);
        }
        return number;
    }

    private static String evaluate(String number) {
        char[] digits = number.toCharArray();

        int repeats = 1;
        char target = digits[0];

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < digits.length; i++) {
            if (digits[i] != target) {
                builder.append(repeats);
                builder.append(target);

                repeats = 0;
                target = digits[i];
            }

            repeats += 1;
        }

        builder.append(repeats);
        builder.append(target);

        return builder.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        long seed = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(seed, N_REPEAT).length());
    }

    private static long getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextLong();
        }
    }
}
