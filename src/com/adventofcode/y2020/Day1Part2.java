package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day1Part2 {

    private static final String INPUT_FILE = "y2020/day1";

    public static long calculate(List<Long> expenses) {
        Long[] numbers = expenses.toArray(new Long[0]);
        for (int i = 0; i < numbers.length; i++) {
            long target = 2020 - numbers[i];
            Set<Long> opposites = new HashSet<>();
            for (int j = i; j < numbers.length; j++) {
                if (opposites.contains(numbers[j])) {
                    return numbers[i] * numbers[j] * (2020 - numbers[i] - numbers[j]);
                }
                opposites.add(target - numbers[j]);
            }
        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Long> expenses = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = calculate(expenses);
        System.out.println(result);
    }

    private static List<Long> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Long> expenses = new ArrayList<>();
            while (scanner.hasNext()) {
                expenses.add(scanner.nextLong());
            }
            return expenses;
        }
    }
}
