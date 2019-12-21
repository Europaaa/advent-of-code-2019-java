package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day16Part2 {

    private static final String INPUT_FILE = "day16";

    private static final int OUTPUT_SIZE = 8;

    public static String getOutput(int[] digits, int phases) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(digits);
        for (int i = 0; i < phases; i++) {
            fft.evaluate();
        }

        return Arrays.stream(Arrays.copyOfRange(fft.digits, fft.offset, fft.offset + OUTPUT_SIZE))
                .mapToObj(String::valueOf).collect(Collectors.joining());
    }

    static class FlawedFrequencyTransmission {

        private static final int OFFSET_SIZE = 7;

        private int[] digits;
        private int offset;

        FlawedFrequencyTransmission(int[] digits) {
            this.offset = getOffset(digits);
            this.digits = digits;
        }

        private int getOffset(int[] digits) {
            return Integer.valueOf(Arrays.stream(Arrays.copyOfRange(digits, 0, OFFSET_SIZE))
                    .mapToObj(String::valueOf).collect(Collectors.joining()));
        }

        public void evaluate() {
            // This algorithm is based on the fact that the offset is past the middle point.
            // It means that when we calculate the rows from the offset onwards, and the pattern
            // digit for row i starts with i 0's and 1 onwards.
            for (int i = this.digits.length - 2; i >= this.offset; i--) {
                this.digits[i] = Math.abs(this.digits[i + 1] + this.digits[i]) % 10;
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] digits = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(getOutput(repeat(digits, 10000), 100));
    }

    private static int[] repeat(int[] digits, int n) {
        int[] newDigits = new int[digits.length * n];
        for (int i = 0; i < newDigits.length; i++) {
            newDigits[i] = digits[i % digits.length];
        }
        return newDigits;
    }

    private static int[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.useDelimiter("");

            List<Integer> digits = new ArrayList<>();
            while (scanner.hasNext()) {
                digits.add(scanner.nextInt());
            }
            return digits.stream().mapToInt(i -> i).toArray();
        }
    }
}
