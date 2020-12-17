package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class Day15Part1 {

    private static final String INPUT_FILE = "y2020/day15";

    public static Integer calculate(int[] numbers) {
        Map<Integer, int[]> indices = new HashMap<>();

        int i = 0, last = 0;
        for (; i < numbers.length; i++) {
            int number = numbers[i];
            indices.put(number, new int[] {i, -1});
            last = number;
        }

        while (i < 2020) {
            int number = 0;

            int[] history = indices.get(last);
            if (history != null && history[1] != -1) {
                number = history[0] - history[1];
            }

            history = indices.getOrDefault(number, new int[] {-1, -1});
            history[1] = history[0];
            history[0] = i;

            indices.put(number, history);
            last = number;
            i++;
        }

        return last;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] numbers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int last = calculate(numbers);
        System.out.println(last);
    }

    private static int[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return Arrays.stream(scanner.nextLine().split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }
    }
}
