package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day1Part1 {

    private static final String INPUT_FILE = "y2015/day1";

    private static final char CODE_UP = '(';
    private static final char CODE_DOWN = ')';

    public static long evaluate(String codes) {
        long ups = codes.chars().filter(c -> c == CODE_UP).count();
        long downs = codes.chars().filter(c -> c == CODE_DOWN).count();
        return ups - downs;
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
