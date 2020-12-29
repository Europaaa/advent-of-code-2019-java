package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day1Part2 {
    private static final String INPUT_FILE = "y2018/day1";

    public static long calculate(Long[] changes) {
        Set<Long> frequencies = new HashSet<>();

        long frequency = 0L;
        for (int i = 0; i < changes.length && !frequencies.contains(frequency); i = (i + 1) % changes.length) {
            frequencies.add(frequency);
            frequency += changes[i];
        }

        return frequency;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Long[] changes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long frequency = calculate(changes);
        System.out.println(frequency);
    }

    private static Long[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Long> changes = new ArrayList<>();
            while (scanner.hasNext()) {
                changes.add(scanner.nextLong());
            }
            return changes.toArray(new Long[0]);
        }
    }
}
