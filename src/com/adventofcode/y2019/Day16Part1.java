package com.adventofcode.y2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day16Part1 {

    private static final String INPUT_FILE = "y2019/day16";

    private static final int OUTPUT_SIZE = 8;

    public static String getOutput(int[] digits, int phases) {
        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission(digits);
        for (int i = 0; i < phases; i++) {
            fft.evaluate();
        }

        return Arrays.stream(Arrays.copyOfRange(fft.digits, 1, 1 + OUTPUT_SIZE))
                .mapToObj(String::valueOf).collect(Collectors.joining());
    }

    static class FlawedFrequencyTransmission {

        private int[] digits;

        FlawedFrequencyTransmission(int[] digits) {
            this.digits = shiftDigits(digits);
        }

        private int[] shiftDigits(int[] digits) {
            int[] newDigits = new int[digits.length + 1];
            newDigits[0] = 0;
            for (int i = 0; i < digits.length; i++) {
                newDigits[i + 1] = digits[i];
            }
            return newDigits;
        }

        public void evaluate() {
            int[] digits = new int[this.digits.length];
            for (int i = 1; i < this.digits.length; i++) {
                digits[i] = getDigit(i);
            }
            this.digits = digits;
        }

        private int getDigit(int i) {
            int n = this.digits.length;

            int sum = 0;
            for (int j = i; j < n; j += 4 * i) {
                for (int k = 0; k < i && j + k < n; k++) {
                    sum += this.digits[j + k];
                }
            }
            for (int j = i * 3; j < n; j += 4 * i) {
                for (int k = 0; k < i && j + k < n; k++) {
                    sum -= this.digits[j + k];
                }
            }
            return Math.abs(sum) % 10;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] digits = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(getOutput(digits, 100));
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
