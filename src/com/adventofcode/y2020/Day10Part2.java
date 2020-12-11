package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day10Part2 {

    private static final String INPUT_FILE = "y2020/day10";

    private Integer[] jolts;
    private long[] memo;

    public Day10Part2(Integer[] jolts) {
        this.jolts = jolts;

        this.memo = new long[jolts.length];
        Arrays.fill(memo, -1);
    }

    public long countArrangement() {
        long count = countArrangement(0);

        if (jolts[1] > 3) {
            return count;
        }
        count += countArrangement( 1);

        if (jolts[2] > 3) {
            return count;
        }
        count += countArrangement( 2);

        return count;
    }

    private long countArrangement(int prev) {
        if (memo[prev] >= 0) {
            return memo[prev];
        }

        if (prev == jolts.length - 1) {
            memo[prev] = 1;
            return 1;
        }

        long count = 0;
        for (int i = prev + 1; i < prev + 4 && i < jolts.length && jolts[i] - jolts[prev] <= 3; i++) {
            count += countArrangement(i);
        }
        memo[prev] = count;
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> jolts = getInput(CommonUtils.getInputFile(INPUT_FILE));
        jolts.sort(Comparator.naturalOrder());

        Day10Part2 day10Part2 = new Day10Part2(jolts.toArray(new Integer[0]));
        long result = day10Part2.countArrangement();
        System.out.println(result);
    }

    private static List<Integer> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Integer> numbers = new ArrayList<>();
            while (scanner.hasNext()) {
                numbers.add(scanner.nextInt());
            }
            return numbers;
        }
    }
}
