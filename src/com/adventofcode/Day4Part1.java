package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day4Part1 {

    private static final String INPUT_FILE = "day4";

    public static int countPossiblePasswords(int min, int max) {
        int count = 0;
        for (int i = min; i <= max; i++) {
            if (isValid(i)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isValid(int number) {
        char[] digits = String.valueOf(number).toCharArray();

        // Check if any two adjacent digits are same
        boolean found = false;
        for (int i = 1; i < digits.length && !found; i++) {
            if (digits[i] == digits[i - 1]) {
                found = true;
            }
        }

        if (!found) {
            return false;
        }

        // Check if the digits never decrease
        char digit = digits[0];
        for (int i = 1; i < digits.length; i++) {
            if (digit > digits[i]) {
                return false;
            }
            digit = digits[i];
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] range = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int count = countPossiblePasswords(range[0], range[1]);
        System.out.println(count);
    }

    private static int[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            String[] tokens = scanner.nextLine().split("-");

            int min = Integer.valueOf(tokens[0]);
            int max = Integer.valueOf(tokens[1]);

            return new int[]{min, max};
        }
    }
}
