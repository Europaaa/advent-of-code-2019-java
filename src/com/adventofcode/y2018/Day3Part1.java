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

public class Day3Part1 {
    private static final String INPUT_FILE = "y2018/day3";

    private static final Pattern PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

    public static long calculate(List<Area> areas) {
        int maxX = areas.stream()
                .mapToInt(area -> area.x + area.w)
                .max().getAsInt();

        int maxY = areas.stream()
                .mapToInt(area -> area.y + area.h)
                .max().getAsInt();

        int[][] coverage = new int[maxY][maxX];
        Arrays.stream(coverage).forEach(counters -> Arrays.fill(counters, 0));

        areas.forEach(area -> {
            for (int y = 0; y < area.h; y++) {
                for (int x = 0; x < area.w; x++) {
                    coverage[area.y + y][area.x + x] += 1;
                }
            }
        });

        return Arrays.stream(coverage)
                .mapToLong(counters -> Arrays.stream(counters).filter(c -> c >= 2).count())
                .sum();
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Area> areas = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long count = calculate(areas);
        System.out.println(count);
    }

    private static class Area {
        private int x;
        private int y;
        private int w;
        private int h;

        public Area(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    private static List<Area> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Area> areas = new ArrayList<>();
            while (scanner.hasNext()) {
                Matcher matcher = PATTERN.matcher(scanner.nextLine());
                if (!matcher.matches()) {
                    continue;
                }

                areas.add(new Area(
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)),
                        Integer.parseInt(matcher.group(5))));
            }
            return areas;
        }
    }
}
