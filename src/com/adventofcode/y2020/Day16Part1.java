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

public class Day16Part1 {

    private static final String INPUT_FILE = "y2020/day16";

    private static final Pattern PATTERN_FIELD = Pattern.compile("([\\w\\s]+):\\s(\\d+)-(\\d+)\\sor\\s(\\d+)-(\\d+)");

    private Map<String, Field> fields;
    private List<int[]> tickets;

    private Day16Part1(Map<String, Field> fields, List<int[]> tickets) {
        this.fields = fields;
        this.tickets = tickets;
    }

    public long validate() {
        return tickets.stream().mapToLong(this::validate).sum();
    }

    private long validate(int[] ticket) {
        return Arrays.stream(ticket)
                .filter(number -> fields.values().stream().noneMatch(f -> f.contains(number)))
                .sum();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day16Part1 day16Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long sum = day16Part1.validate();
        System.out.println(sum);
    }

    private static Day16Part1 getInput(String path) throws FileNotFoundException {
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
            scanner.nextLine();
            scanner.nextLine();
            scanner.nextLine();

            List<int[]> tickets = new ArrayList<>();
            while (scanner.hasNext()) {
                tickets.add(Arrays.stream(scanner.nextLine().trim().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray());
            }

            return new Day16Part1(fields, tickets);
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
