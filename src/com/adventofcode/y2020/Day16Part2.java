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

public class Day16Part2 {

    private static final String INPUT_FILE = "y2020/day16";

    private static final Pattern PATTERN_FIELD = Pattern.compile("([\\w\\s]+):\\s(\\d+)-(\\d+)\\sor\\s(\\d+)-(\\d+)");

    private Map<String, Field> fields;
    private int[] myTicket;
    private List<int[]> tickets;

    private Day16Part2(Map<String, Field> fields, int[] myTicket, List<int[]> tickets) {
        this.fields = fields;
        this.myTicket = myTicket;
        this.tickets = tickets;
    }

    public Map<String, Integer> guessOrder() {
        String[] names = fields.keySet().toArray(new String[0]);

        // candidates[i][j] = if names[i] can possible be the field at index j
        boolean[][] candidates = new boolean[names.length][names.length];
        Arrays.stream(candidates).forEach(f -> Arrays.fill(f, true));

        // For every number in every ticket, check if it violates the rule for a field
        for (int[] ticket: tickets) {
            // Skip invalid tickets
            if (this.validate(ticket) != 0) {
                continue;
            }

            for (int i = 0; i < ticket.length; i++) {
                int number = ticket[i];

                for (int f = 0; f < names.length; f++) {
                    Field field = fields.get(names[f]);
                    if (!field.contains(number)) {
                        candidates[f][i] = false;
                    }
                }
            }
        }

        // Stores the positions of each field
        Map<String, Integer> positions = new HashMap<>();

        while (positions.size() < fields.size()) {
            // Because there's a unique solution, there must be a field with exact one valid position
            // We must be able to finalize the position for one field per iteration
            for (int i = 0; i < candidates.length; i++) {
                // Skip fields we already finalized
                if (positions.containsKey(names[i])) {
                    continue;
                }

                // Skip if the field has multiple possible positions
                boolean[] candidate = candidates[i];
                int solution = getUniquePosition(candidate);
                if (solution == -1) {
                    continue;
                }

                // The field has a unique position. Mark the position as invalid for all other fields
                positions.put(names[i], solution);
                for (int j = 0; j < candidate.length; j++) {
                    candidates[j][solution] = false;
                }

                break;
            }
        }
        return positions;
    }

    private long validate(int[] ticket) {
        return Arrays.stream(ticket)
                .filter(number -> fields.values().stream().noneMatch(f -> f.contains(number)))
                .sum();
    }

    private int getUniquePosition(boolean[] candidates) {
        int solution = -1;
        for (int i = 0; i < candidates.length; i++) {
            if (!candidates[i]) {
                continue;
            }
            if (solution != -1) {
                return -1;
            }
            solution = i;
        }
        return solution;
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day16Part2 day16Part2 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Map<String, Integer> orders = day16Part2.guessOrder();

        long product = 1;
        for (String name : orders.keySet()) {
            if (!name.startsWith("departure")) {
                continue;
            }

            int index = orders.get(name);
            product *= day16Part2.myTicket[index];
        }
        System.out.println(product);
    }

    private static Day16Part2 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Map<String, Field> fields = new HashMap<>();

            String line = scanner.nextLine().trim();
            while (!line.isEmpty()) {
                Matcher matcher = PATTERN_FIELD.matcher(line);
                if (matcher.matches()) {
                    Field field = new Field()
                            .setRange1Start(Integer.parseInt(matcher.group(2)))
                            .setRange1End(Integer.parseInt(matcher.group(3)))
                            .setRange2Start(Integer.parseInt(matcher.group(4)))
                            .setRange2End(Integer.parseInt(matcher.group(5)));

                    fields.put(matcher.group(1), field);
                }
                line = scanner.nextLine().trim();
            }

            scanner.nextLine();

            int[] myTicket = Arrays.stream(scanner.nextLine().trim().split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            scanner.nextLine();
            scanner.nextLine();

            List<int[]> tickets = new ArrayList<>();
            while (scanner.hasNext()) {
                tickets.add(Arrays.stream(scanner.nextLine().trim().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray());
            }

            return new Day16Part2(fields, myTicket, tickets);
        }
    }

    private static class Field {
        private int range1Start;
        private int range1End;
        private int range2Start;
        private int range2End;

        public Field setRange1Start(int range1Start) {
            this.range1Start = range1Start;
            return this;
        }

        public Field setRange1End(int range1End) {
            this.range1End = range1End;
            return this;
        }

        public Field setRange2Start(int range2Start) {
            this.range2Start = range2Start;
            return this;
        }

        public Field setRange2End(int range2End) {
            this.range2End = range2End;
            return this;
        }

        public boolean contains(int number) {
            return (range1Start <= number && number <= range1End) || (range2Start <= number && number <= range2End);
        }
    }
}
