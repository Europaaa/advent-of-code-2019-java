package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Day5Part1 {
    private static final String INPUT_FILE = "y2018/day5";

    private static final int DIFF = 'a' - 'A';

    public static String calculate(char[] units) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < units.length; i++) {
            char unit = units[i];

            if (stack.isEmpty()) {
                stack.push(unit);
                continue;
            }

            if (matches(unit, stack.peek())) {
                stack.pop();
            } else {
                stack.push(unit);
            }
        }

        StringBuilder builder = new StringBuilder();
        stack.forEach(builder::append);
        return builder.toString();
    }

    private static boolean matches(char c1, char c2) {
        if (c1 == c2) {
            return false;
        }
        return c1 + DIFF == c2 || c2 + DIFF == c1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[] units = getInput(CommonUtils.getInputFile(INPUT_FILE));

        String result = calculate(units);
        System.out.println(result.length());
    }

    private static char[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextLine().toCharArray();
        }
    }
}
