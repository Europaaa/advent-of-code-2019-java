package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12Part2 {
    private static final String INPUT_FILE = "y2018/day12";

    private static final Pattern PATTERN_STATE = Pattern.compile("initial state: ([.#]+)");
    private static final Pattern PATTERN_RULE = Pattern.compile("([.#]{5}) => ([.#])");

    private Pot[] pots;
    private List<Rule> rules;

    public Day12Part2(Pot[] pots, List<Rule> rules) {
        this.pots = pots;
        this.rules = rules;

        this.setupPots();
    }

    public void simulate() {
        // Calculate next states
        char[] states = new char[pots.length];
        for (int i = 0; i < pots.length; i++) {
            states[i] = getNextState(pots[i]);
        }

        // Update next states
        for (int i = 0; i < pots.length; i++) {
            pots[i].state = states[i];
        }

        setupPots();
    }

    // Extend the pot array so that there are 5 empty pots at the left-most and right-most side
    private void setupPots() {
        int n = pots.length;

        int l = 0;
        for (; l < 5 && l < n && pots[l].state == '.'; l++) {
        }
        int nL = 5 - l;

        int r = 0;
        for (; r < 5 && n - r - 1 >= 0 && pots[n - r - 1].state == '.'; r++) {
        }
        int nR = 5 - r;

        Pot[] pots = new Pot[n + nL + nR];
        for (int i = 0; i < this.pots.length; i++) {
            pots[i + nL] = this.pots[i];
        }

        Pot left = this.pots[0];
        for (int i = 0; i < nL; i++) {
            Pot p = new Pot(left.id - 1, '.');
            pots[nL - i - 1] = p;

            left.left = p;
            left = left.left;
        }
        for (int i = 0; i < nL; i++) {
            pots[i].right = pots[i + 1];
        }

        Pot right = this.pots[n - 1];
        for (int i = 0; i < nR; i++) {
            pots[n + nL + i] = new Pot(right.id + 1, '.');

            right.right = pots[n + nL + i];
            right = right.right;
        }
        for (int i = 0; i < nR; i++) {
            pots[n + nL + i].left = pots[n + nL + i - 1];
        }

        this.pots = pots;
    }

    private char getNextState(Pot pot) {
        String hash = pot.getHash();

        return rules.stream()
                .filter(r -> r.from.equals(hash))
                .findFirst()
                .map(r -> r.to)
                .orElse('.');
    }

    private static class Pot {
        private int id;
        private char state;

        private Pot left;
        private Pot right;

        public Pot(int id, char state) {
            this.id = id;
            this.state = state;
        }

        private String getHash() {
            StringBuilder builder = new StringBuilder();

            Pot pot = this;
            for (int i = 0; i <= 2; i++) {
                if (pot == null) {
                    builder.append('.');
                } else {
                    builder.append(pot.state);
                    pot = pot.left;
                }
            }
            builder = builder.reverse();

            pot = this.right;
            for (int i = 0; i < 2; i++) {
                if (pot == null) {
                    builder.append('.');
                } else {
                    builder.append(pot.state);
                    pot = pot.right;
                }
            }
            return builder.toString();
        }
    }

    private static class Rule {
        private String from;
        private char to;

        private Rule(String from, char to) {
            this.from = from;
            this.to = to;
        }
    }

    public static long calculate(Day12Part2 day12Part1, long rounds, int convergeFactor) {
        long count = 1, diff = 0;

        long i = 0;
        long prev = 0;
        for (; i < rounds && count < convergeFactor; i++) {
            day12Part1.simulate();

            long sum = Arrays.stream(day12Part1.pots)
                    .filter(p -> p.state == '#')
                    .mapToLong(p -> p.id)
                    .sum();

            if (sum - prev == diff) {
                count += 1;
            } else {
                diff = sum - prev;
                count = 1;
            }
            prev = sum;
        }

        if (i == rounds) {
            return prev;
        }

        // Monitor the change between the sum. If the diff between rounds remain the same for more than a specified
        // amount of times (i.e. convergeFactor), then we assume the difference has converged
        return prev + diff * (rounds - i);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day12Part2 day12Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long sum = calculate(day12Part1, 50000000000L, 50);
        System.out.println(sum);
    }

    private static Day12Part2 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Matcher stateMatcher = PATTERN_STATE.matcher(scanner.nextLine());
            if (!stateMatcher.matches()) {
                throw new IllegalStateException();
            }

            char[] state = stateMatcher.group(1).toCharArray();

            Pot[] pots = new Pot[state.length];
            for (int i = 0; i < state.length; i++) {
                pots[i] = new Pot(i, state[i]);
            }

            pots[0].right = pots[1];
            for (int i = 1; i < state.length - 1; i++) {
                pots[i].left = pots[i - 1];
                pots[i].right = pots[i + 1];
            }
            pots[pots.length - 1].left = pots[pots.length - 2];

            scanner.nextLine();

            List<Rule> rules = new ArrayList<>();
            while (scanner.hasNext()) {
                Matcher ruleMatcher = PATTERN_RULE.matcher(scanner.nextLine());
                if (!ruleMatcher.matches()) {
                    throw new IllegalStateException();
                }

                rules.add(new Rule(ruleMatcher.group(1), ruleMatcher.group(2).charAt(0)));
            }
            return new Day12Part2(pots, rules);
        }
    }
}
