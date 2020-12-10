package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day9Part2 {

    private static final String INPUT_FILE = "y2020/day9";

    private static int[] findContiguousSet(Long[] numbers, Long target) {
        int i = 0, j = 0;
        long sum = 0;

        while (i <= j && j < numbers.length && sum != target) {
            if (sum < target) {
                sum += numbers[j];
                j += 1;
            } else {
                sum -= numbers[i];
                i += 1;
            }
        }
        return new int[]{i, j};
    }

    private static long findBadNumber(Long[] numbers) {
        for (int i = 25; i < numbers.length; i++) {
            boolean match = false;
            for (int j = i - 25; j < i; j++) {
                for (int k = j; k < i; k++) {
                    if (numbers[j] + numbers[k] == numbers[i]) {
                        match = true;
                        break;
                    }
                }
            }

            if (!match) {
                return numbers[i];
            }
        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Long[] numbers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long target = findBadNumber(numbers);
        int[] range = findContiguousSet(numbers, target);

        long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
        for (int i = range[0]; i < range[1]; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        System.out.println(min + max);
    }

    private static Long[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Long> numbers = new ArrayList<>();
            while (scanner.hasNext()) {
                numbers.add(scanner.nextLong());
            }
            return numbers.toArray(new Long[0]);
        }
    }
}
