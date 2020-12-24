package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day24Part1 {

    private static final String INPUT_FILE = "y2020/day24";

    private enum Direction {
        W(-1, 0),
        NW(-1, 1),
        NE(0, 1),
        E(1, 0),
        SE(1, -1),
        SW(0, -1);

        private int x;
        private int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final int COLOR_WHITE = 0;
    private static final int COLOR_BLACK = 1;

    private Map<String, Integer> colors;

    public Day24Part1() {
        this.colors = new HashMap<>();
        this.colors.put("E0,SE0,SW0", COLOR_WHITE);
    }

    public void flip(List<Direction> directions) {
        String hash = generateHash(directions);

        int color = colors.getOrDefault(hash, COLOR_WHITE);
        colors.put(hash, 1 - color);
    }

    private String generateHash(List<Direction> directions) {
        long x = 0;
        long y = 0;

        for (Direction direction : directions) {
            x += direction.x;
            y += direction.y;
        }
        return x + "," + y;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<Direction>> instructions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Day24Part1 day24Part1 = new Day24Part1();

        for (List<Direction> directions : instructions) {
            day24Part1.flip(directions);
        }

        long count = day24Part1.colors.values().stream()
                .filter(color -> COLOR_BLACK == color)
                .count();
        System.out.println(count);
    }

    private static List<List<Direction>> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<List<Direction>> instructions = new ArrayList<>();

            while (scanner.hasNext()) {
                char[] chars = scanner.nextLine().toCharArray();

                List<Direction> directions = new LinkedList<>();
                for (int i = 0; i < chars.length; i++) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(chars[i]);

                    switch (chars[i]) {
                        case 'n':
                        case 's':
                            builder.append(chars[++i]);
                            break;
                    }

                    directions.add(Direction.valueOf(builder.toString().toUpperCase()));
                }

                instructions.add(directions);
            }
            return instructions;
        }
    }
}
