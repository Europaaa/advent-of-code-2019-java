package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8Part2 {

    private static final String INPUT_FILE = "y2020/day8";

    private static final Pattern PATTERN = Pattern.compile("(acc|jmp|nop)\\s([+-])(\\d+)");

    public static long fixProgram(String[] instructions) {
        long[] results = getAccumulator(instructions);

        long accumulator = results[0];
        int pointer = (int) results[1];

        int i = 0;

        while (pointer != instructions.length) {
            String instruction = instructions[i];

            Matcher matcher = PATTERN.matcher(instruction);
            if (!matcher.matches()) {
                System.out.println(instruction);
                continue;
            }

            String cmd = matcher.group(1);
            int sign = "+".equals(matcher.group(2)) ? 1 : -1;
            int value = Integer.valueOf(matcher.group(3));

            // Only possible way to get in loop if we jump to the back or fall into single-instruction loop
            if ("jmp".equals(cmd) && (sign == -1 || value == 0)) {
                String backupCommand = instructions[i];
                instructions[i] = "nop " + sign + value;

                results = getAccumulator(instructions);
                accumulator = results[0];
                pointer = (int) results[1];

                instructions[i] = backupCommand;
            }

            switch (cmd) {
                case "acc":
                case "nop":
                    i += 1;
                    break;
                case "jmp":
                    i = i + sign * value;
                    break;
            }
        }
        return accumulator;
    }

    private static long[] getAccumulator(String[] instructions) {
        boolean[] visited = new boolean[instructions.length];
        Arrays.fill(visited, false);

        int i = 0;
        long accumulator = 0;
        while (i >= 0 && i < instructions.length && !visited[i]) {
            visited[i] = true;

            String instruction = instructions[i];

            Matcher matcher = PATTERN.matcher(instruction);
            if (!matcher.matches()) {
                System.out.println(instruction);
                continue;
            }

            String cmd = matcher.group(1);
            int sign = "+".equals(matcher.group(2)) ? 1 : -1;
            int value = Integer.valueOf(matcher.group(3));

            switch (cmd) {
                case "acc":
                    accumulator += sign * value;
                    i += 1;
                    break;
                case "jmp":
                    i = i + sign * value;
                    break;
                case "nop":
                    i += 1;
                    break;
            }
        }
        return new long[]{accumulator, i};
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> instructions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = fixProgram(instructions.toArray(new String[0]));
        System.out.println(result);
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> instructions = new ArrayList<>();
            while (scanner.hasNext()) {
                instructions.add(scanner.nextLine());
            }
            return instructions;
        }
    }
}
