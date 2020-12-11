package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Day10Part1 {

    private static final String INPUT_FILE = "y2020/day10";

    public static int[] getDifferences(Integer[] jolts) {
        int[] differences = new int[3];
        Arrays.fill(differences, 0);

        differences[jolts[0] - 1] = 1;
        for (int i = 1; i < jolts.length; i++) {
            differences[jolts[i] - jolts[i - 1] - 1] += 1;
        }
        differences[2]++;
        return differences;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> jolts = getInput(CommonUtils.getInputFile(INPUT_FILE));
        jolts.sort(Comparator.naturalOrder());

        int[] result = getDifferences(jolts.toArray(new Integer[0]));
        System.out.println(result[0] * result[2]);
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
