package com.adventofcode.y2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day2Part2 {

    private static final String INPUT_FILE = "y2019/day2";

    private static final int TARGET = 19690720;

    public static int[] evaluate(Integer[] codes, int target) {
        for (int noun = 1; noun < 100; noun++) {
            for (int verb = 1; verb < 100; verb++) {
                Integer[] attempt = Arrays.copyOf(codes, codes.length);
                attempt[1] = noun;
                attempt[2] = verb;

                if (target == evaluate(attempt)) {
                    return new int[]{noun, verb};
                }
            }
        }
        throw new IllegalArgumentException("No solution found.");
    }

    public static int evaluate(Integer[] codes) {
        int i = 0;
        while (i < codes.length) {
            switch (codes[i]) {
                case 1:
                    codes[codes[i + 3]] = codes[codes[i + 1]] + codes[codes[i + 2]];
                    i = i + 4;
                    break;
                case 2:
                    codes[codes[i + 3]] = codes[codes[i + 1]] * codes[codes[i + 2]];
                    i = i + 4;
                    break;
                case 99:
                    return codes[0];
                default:
                    throw new IllegalArgumentException("Invalid input for the program");
            }
        }
        return codes[0];
    }

    public static void main(String[] args) throws FileNotFoundException {
        Integer[] codes = getInput(CommonUtils.getInputFile(INPUT_FILE)).toArray(new Integer[0]);

        int[] values = evaluate(codes, TARGET);
        System.out.println(values[0] * 100 + values[1]);
    }

    private static List<Integer> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.useDelimiter(",");
            List<Integer> codes = new ArrayList<>();
            while (scanner.hasNext()) {
                codes.add(scanner.nextInt());
            }
            return codes;
        }
    }
}
