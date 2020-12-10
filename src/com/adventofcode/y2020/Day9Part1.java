package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day9Part1 {

    private static final String INPUT_FILE = "y2020/day9";

    public static long findBadNumber(Long[] numbers) {
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
        List<Long> numbers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = findBadNumber(numbers.toArray(new Long[0]));
        System.out.println(result);
    }

    private static List<Long> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Long> numbers = new ArrayList<>();
            while (scanner.hasNext()) {
                numbers.add(scanner.nextLong());
            }
            return numbers;
        }
    }
}
