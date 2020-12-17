package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14Part2 {

    private static final String INPUT_FILE = "y2020/day14";

    private static final int SIZE = 36;

    private static final Pattern PATTERN_MASK = Pattern.compile("mask = ([01X]{" + SIZE + "})");
    private static final Pattern PATTERN_MEM = Pattern.compile("mem\\[(\\d+)] = (\\d+)");

    public static Map<Long, Long> update(List<Command> commands) {
        Map<Long, Long> memory = new HashMap<>();
        for (Command command : commands) {
            update(command, memory);
        }
        return memory;
    }

    private static void update(Command command, Map<Long, Long> memory) {
        // Apply the bit mask
        char[] bits = evaluate(command);

        Set<Long> addresses = new HashSet<>();
        update(bits, 0, addresses);

        addresses.forEach(address -> memory.put(address, command.value));
    }

    private static char[] evaluate(Command command) {
        char[] bits = new char[SIZE];

        for (int i = 0; i < command.address.length; i++) {
            bits[i] = command.address[i];
        }
        for (int i = 0; i < SIZE; i++) {
            if (command.mask[i] == 'X') {
                bits[i] = 'X';
                continue;
            }
            if (command.mask[i] == '1') {
                bits[i] = '1';
            }
        }
        return bits;
    }

    private static void update(char[] bits, int i, Set<Long> addresses) {
        if (i == SIZE) {
            addresses.add(Long.parseLong(new String(bits), 2));
            return;
        }

        if (bits[i] != 'X') {
            update(bits, i + 1, addresses);
            return;
        }

        bits[i] = '0';
        update(bits, i + 1, addresses);

        bits[i] = '1';
        update(bits, i + 1, addresses);

        bits[i] = 'X';
    }

    static class Command {
        private char[] mask;
        private Long value;
        private char[] address;

        public Command setMask(char[] mask) {
            this.mask = mask;
            return this;
        }

        public Command setValue(Long value) {
            this.value = value;
            return this;
        }

        public Command setAddress(char[] address) {
            this.address = address;
            return this;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Command> commands = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Map<Long, Long> memory = update(commands);
        long sum = memory.values().stream().mapToLong(i -> i).sum();
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
                            .setAddress(toBinaryString(memMatcher.group(1)))
                            .setValue(Long.parseLong(memMatcher.group(2))));
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
