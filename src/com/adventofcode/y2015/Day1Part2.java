package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day1Part2 {

    private static final String INPUT_FILE = "y2015/day1";

    private static final char CODE_UP = '(';
    private static final char CODE_DOWN = ')';

    public static long evaluate(String codes) {
        char[] chars = codes.toCharArray();

        long ups = 0;
        long downs = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == CODE_UP) {
                ups += 1;
            }
            if (chars[i] == CODE_DOWN) {
                downs += 1;
            }

            if (ups < downs) {
                return i + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String codes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(codes));
    }

    private static String getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextLine();
        }
    }
}
