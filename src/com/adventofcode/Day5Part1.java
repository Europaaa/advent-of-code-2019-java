package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day5Part1 {

    private static final String INPUT_FILE = "day5";

    public static void evaluate(Integer[] codes, int systemId) {
        int i = 0;
        while (i < codes.length) {
            Integer optcode = codes[i];

            switch (optcode % 100) {
                case 1:
                    char[] modes = String.format("%05d", optcode).toCharArray();
                    int op1 = modes[2] == '1' ? i + 1 : codes[i + 1];
                    int op2 = modes[1] == '1' ? i + 2 : codes[i + 2];
                    int op3 = modes[0] == '1' ? i + 3 : codes[i + 3];
                    codes[op3] = codes[op1] + codes[op2];
                    i = i + 4;
                    break;
                case 2:
                    modes = String.format("%05d", optcode).toCharArray();
                    op1 = modes[2] == '1' ? i + 1 : codes[i + 1];
                    op2 = modes[1] == '1' ? i + 2 : codes[i + 2];
                    op3 = modes[0] == '1' ? i + 3 : codes[i + 3];
                    codes[op3] = codes[op1] * codes[op2];
                    i = i + 4;
                    break;
                case 3:
                    modes = String.format("%03d", optcode).toCharArray();
                    op1 = modes[0] == '1' ? i + 1 : codes[i + 1];
                    codes[op1] = systemId;
                    i = i + 2;
                    break;
                case 4:
                    modes = String.format("%03d", optcode).toCharArray();
                    op1 = modes[0] == '1' ? i + 1 : codes[i + 1];
                    System.out.println(codes[op1]);
                    i = i + 2;
                    break;
                case 99:
                    return;
                default:
                    throw new IllegalArgumentException("Invalid input for the program");
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Integer[] codes = getInput(CommonUtils.getInputFile(INPUT_FILE)).toArray(new Integer[0]);

        evaluate(codes, 1);
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
