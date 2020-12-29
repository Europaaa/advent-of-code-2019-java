package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day2Part1 {
    private static final String INPUT_FILE = "y2018/day2";

    public static long calculate(List<char[]> ids) {
        long count1 = 0;
        long count2 = 0;

        for (char[] letters : ids) {
            Map<Character, Long> counters = new HashMap<>();
            for (char letter : letters) {
                counters.put(letter, counters.getOrDefault(letter, 0L) + 1);
            }

            if (counters.values().contains(2L)) {
                count1 += 1;
            }
            if (counters.values().contains(3L)) {
                count2 += 1;
            }
        }
        return count1 * count2;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<char[]> ids = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long frequency = calculate(ids);
        System.out.println(frequency);
    }

    private static List<char[]> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<char[]> ids = new ArrayList<>();
            while (scanner.hasNext()) {
                ids.add(scanner.nextLine().toCharArray());
            }
            return ids;
        }
    }
}
