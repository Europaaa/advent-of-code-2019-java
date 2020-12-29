package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1Part1 {
    private static final String INPUT_FILE = "y2018/day1";

    public static long calculate(List<Long> changes) {
        return changes.stream().mapToLong(c -> c).sum();
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Long> changes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long frequency = calculate(changes);
        System.out.println(frequency);
    }

    private static List<Long> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Long> changes = new ArrayList<>();
            while (scanner.hasNext()) {
                changes.add(scanner.nextLong());
            }
            return changes;
        }
    }
}
