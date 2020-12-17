package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14Part1 {

    private static final String INPUT_FILE = "y2020/day14";

    private static final int SIZE = 36;

    private static final Pattern PATTERN_MASK = Pattern.compile("mask = ([01X]{" + SIZE + "})");
    private static final Pattern PATTERN_MEM = Pattern.compile("mem\\[(\\d+)] = (\\d+)");

    public static Map<Integer, String> update(List<Command> commands) {
        Map<Integer, String> memory = new HashMap<>();

        for (Command command : commands) {
            memory.put(command.address, evaluate(command));
        }
        return memory;
    }

    private static String evaluate(Command command) {
        char[] bits = new char[SIZE];

        for (int i = 0; i < command.value.length; i++) {
            bits[i] = command.value[i];
        }
        for (int i = 0; i < SIZE; i++) {
            if (command.mask[i] == 'X') {
                continue;
            }
            bits[i] = command.mask[i];
        }

        return new String(bits);
    }

    static class Command {
        private char[] mask;
        private char[] value;
        private int address;

        public Command setMask(char[] mask) {
            this.mask = mask;
            return this;
        }

        public Command setValue(char[] value) {
            this.value = value;
            return this;
        }

        public Command setAddress(int address) {
            this.address = address;
            return this;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Command> commands = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Map<Integer, String> memory = update(commands);
        long sum = memory.values().stream().mapToLong(i -> Long.parseLong(i, 2)).sum();
        System.out.println(sum);
    }

    private static List<Command> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Command> commands = new ArrayList<>();

            char[] mask = null;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                Matcher maskMatcher = PATTERN_MASK.matcher(line);
                if (maskMatcher.matches()) {
                    mask = maskMatcher.group(1).toCharArray();
                    continue;
                }

                Matcher memMatcher = PATTERN_MEM.matcher(line);
                if (memMatcher.matches()) {
                    commands.add(new Command()
                            .setMask(mask)
                            .setAddress(Integer.parseInt(memMatcher.group(1)))
                            .setValue(toBinaryString(memMatcher.group(2))));
                }
            }
            return commands;
        }
    }

    private static char[] toBinaryString(String number) {
        String binaryString = Integer.toBinaryString(Integer.parseInt(number));

        char[] value = new char[SIZE];
        Arrays.fill(value, '0');

        char[] chars = binaryString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            value[SIZE - chars.length + i] = chars[i];
        }
        return value;
    }
}
