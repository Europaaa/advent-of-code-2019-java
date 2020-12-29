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
import java.util.stream.Collectors;

public class Day10Part2 {
    private static final String INPUT_FILE = "y2018/day10";

    private static final Pattern PATTERN = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>");

    private List<Point> points;

    public Day10Part2(List<Point> points) {
        this.points = points;
    }

    public String calculate() {
        int time = 0;

        // The image forms when the max distance (in x) of points reaches the minimum
        int lastWidth = Integer.MAX_VALUE;
        while (true) {
            int t = time;
            List<int[]> positions = points.stream()
                    .map(p -> new int[]{p.x + p.vX * t, p.y + p.vY * t})
                    .collect(Collectors.toList());

            int minX = positions.stream().mapToInt(p -> p[0]).min().getAsInt();
            int maxX = positions.stream().mapToInt(p -> p[0]).max().getAsInt();

            int width = maxX - minX + 1;
            if (width >= lastWidth) {
                break;
            }

            lastWidth = width;
            time += 1;
        }
        return toString(time - 1);
    }

    public String toString(int time) {
        List<int[]> positions = points.stream()
                .map(p -> new int[]{p.x + p.vX * time, p.y + p.vY * time})
                .collect(Collectors.toList());

        int minX = positions.stream().mapToInt(p -> p[0]).min().getAsInt();
        int minY = positions.stream().mapToInt(p -> p[1]).min().getAsInt();
        int maxX = positions.stream().mapToInt(p -> p[0]).max().getAsInt();
        int maxY = positions.stream().mapToInt(p -> p[1]).max().getAsInt();

        char[][] message = new char[maxY - minY + 1][maxX - minX + 1];
        Arrays.stream(message).forEach(row -> Arrays.fill(row, '.'));

        positions.forEach(p -> {
            int x = p[0] - minX;
            int y = p[1] - minY;

            message[y][x] = '#';
        });

        StringBuilder builder = new StringBuilder();
        builder.append("Time=").append(time).append("\n");
        Arrays.stream(message).forEach(row -> builder.append(new String(row)).append("\n"));
        
        return builder.toString();
    }

    private static class Point {
        private int x;
        private int y;

        private int vX;
        private int vY;

        public Point(int x, int y, int vX, int vY) {
            this.x = x;
            this.y = y;
            this.vX = vX;
            this.vY = vY;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day10Part2 day10Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        String image = day10Part1.calculate();
        System.out.println(image);
    }

    private static Day10Part2 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Point> points = new ArrayList<>();

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                Matcher matcher = PATTERN.matcher(line);
                if (!matcher.matches()) {
                    System.out.println(line);
                    throw new IllegalStateException();
                }

                points.add(new Point(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4))));
            }
            return new Day10Part2(points);
        }
    }
}
