package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2Part1 {

    private static final String INPUT_FILE = "y2019/day2";

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
        codes[1] = 12;
        codes[2] = 2;

        System.out.println(evaluate(codes));
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
