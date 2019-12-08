package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day7Part1 {

    private static final String INPUT_FILE = "day7";

    public static int getMaxSignal(Integer[] codes) {
        Set<Integer> numbers = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4));
        List<int[]> permutations = getPermutations(numbers, new int[0]);

        int signal = getSignal(codes, permutations.get(0));
        for (int[] phases : permutations) {
            signal = Math.max(signal, getSignal(codes, phases));
        }
        return signal;
    }

    private static List<int[]> getPermutations(Set<Integer> candidates, int[] current) {
        if (candidates.isEmpty()) {
            return Arrays.asList(current);
        }

        List<int[]> permutations = new ArrayList<>();
        for (Integer candidate : candidates) {
            HashSet<Integer> newCandidates = new HashSet<>(candidates);
            newCandidates.remove(candidate);

            int[] permutation = Arrays.copyOf(current, current.length + 1);
            permutation[current.length] = candidate;

            permutations.addAll(getPermutations(newCandidates, permutation));
        }
        return permutations;
    }

    private static Integer getSignal(Integer[] codes, int[] phases) {
        int signal = 0;
        for (int i = 0; i < phases.length; i++) {
            signal = evaluate(codes, new int[]{phases[i], signal});
        }
        return signal;
    }

    private static int evaluate(Integer[] codes, int[] inputs) {
        int i = 0;
        int j = 0;
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
                    codes[op1] = inputs[j++];
                    i = i + 2;
                    break;
                case 4:
                    modes = String.format("%03d", optcode).toCharArray();
                    op1 = modes[0] == '1' ? i + 1 : codes[i + 1];
                    return codes[op1];
                case 5:
                    modes = String.format("%04d", optcode).toCharArray();
                    op1 = modes[1] == '1' ? i + 1 : codes[i + 1];
                    op2 = modes[0] == '1' ? i + 2 : codes[i + 2];
                    if (codes[op1] != 0) {
                        i = codes[op2];
                    } else {
                        i = i + 3;
                    }
                    break;
                case 6:
                    modes = String.format("%04d", optcode).toCharArray();
                    op1 = modes[1] == '1' ? i + 1 : codes[i + 1];
                    op2 = modes[0] == '1' ? i + 2 : codes[i + 2];
                    if (codes[op1] == 0) {
                        i = codes[op2];
                    } else {
                        i = i + 3;
                    }
                    break;
                case 7:
                    modes = String.format("%05d", optcode).toCharArray();
                    op1 = modes[2] == '1' ? i + 1 : codes[i + 1];
                    op2 = modes[1] == '1' ? i + 2 : codes[i + 2];
                    op3 = modes[0] == '1' ? i + 3 : codes[i + 3];
                    codes[op3] = codes[op1] < codes[op2] ? 1 : 0;
                    i = i + 4;
                    break;
                case 8:
                    modes = String.format("%05d", optcode).toCharArray();
                    op1 = modes[2] == '1' ? i + 1 : codes[i + 1];
                    op2 = modes[1] == '1' ? i + 2 : codes[i + 2];
                    op3 = modes[0] == '1' ? i + 3 : codes[i + 3];
                    codes[op3] = codes[op1].intValue() == codes[op2] ? 1 : 0;
                    i = i + 4;
                    break;
                case 99:
                    throw new IllegalStateException("Program should never halt");
                default:
                    throw new IllegalArgumentException("Invalid input for the program");
            }
        }
        throw new IllegalStateException("Program should stop when it output values");
    }

    public static void main(String[] args) throws FileNotFoundException {
        Integer[] codes = getInput(CommonUtils.getInputFile(INPUT_FILE)).toArray(new Integer[0]);

        int maxSignal = getMaxSignal(codes);
        System.out.println(maxSignal);
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
