package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day24Part2 {

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

    public Day24Part2() {
        this.colors = new HashMap<>();
    }

    public void flip(List<Direction> directions) {
        String hash = generateHash(directions);

        int color = colors.getOrDefault(hash, COLOR_WHITE);
        colors.put(hash, 1 - color);
    }

    public void evolve() {
        // Find all the adjacent tiles
        Set<String> hashes = new HashSet<>(this.colors.keySet());
        for (String hash : hashes) {
            int[] location = getLocation(hash);

            int x = location[0];
            int y = location[1];

            // Add adjacent tiles to the queue to check
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == j) {
                        continue;
                    }

                    String adjacentHash = generateHash(x + i, y + j);
                    if (!colors.containsKey(adjacentHash)) {
                        colors.put(adjacentHash, COLOR_WHITE);
                    }
                }
            }
        }

        Map<String, Integer> newColors = new HashMap<>();

        for (String hash : this.colors.keySet()) {
            int[] location = getLocation(hash);

            int x = location[0];
            int y = location[1];

            // Count how many adjacent tiles are black
            int count = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == j) {
                        continue;
                    }

                    int color = this.colors.getOrDefault(generateHash(x + i, y + j), COLOR_WHITE);
                    if (color == COLOR_BLACK) {
                        count += 1;
                    }
                }
            }

            // Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.
            if (this.colors.get(hash) == COLOR_WHITE) {
                if (count == 2) {
                    newColors.put(hash, COLOR_BLACK);
                } else {
                    newColors.put(hash, COLOR_WHITE);
                }
            }
            // Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
            else {
                if (count == 0 || count > 2) {
                    newColors.put(hash, COLOR_WHITE);
                } else {
                    newColors.put(hash, COLOR_BLACK);
                }
            }
        }

        this.colors = newColors;
    }

    private int[] getLocation(String hash) {
        return Arrays.stream(hash.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private String generateHash(int x, int y) {
        return x + "," + y;
    }

    private String generateHash(List<Direction> directions) {
        int x = 0;
        int y = 0;

        for (Direction direction : directions) {
            x += direction.x;
            y += direction.y;
        }
        return generateHash(x, y);
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<Direction>> instructions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Day24Part2 day24Part1 = new Day24Part2();

        for (List<Direction> directions : instructions) {
            day24Part1.flip(directions);
        }

        for (int i = 0; i < 100; i++) {
            day24Part1.evolve();
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
