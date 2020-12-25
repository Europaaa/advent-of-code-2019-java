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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day20Part1 {

    private static final String INPUT_FILE = "y2020/day20";

    private static final Pattern PATTERN_ID = Pattern.compile("Tile\\s(\\d+):");

    private static class Tile {
        private int id;
        private char[][] images;

        private Map<String, Integer> borders;

        public Tile(int id, char[][] images) {
            this.id = id;
            this.images = images;
        }

        public Tile setBorders() {
            int maxI = images.length - 1;

            this.borders = new HashMap<>();

            // Top border from left to right
            this.borders.put(new String(images[0]), -1);

            // Bottom border from right to left
            StringBuilder builder = new StringBuilder();
            for (int i = maxI; i >= 0; i--) {
                builder.append(images[maxI][i]);
            }
            this.borders.put(builder.toString(), -1);

            // Left border from bottom to top
            builder = new StringBuilder();
            for (int i = maxI; i >= 0; i--) {
                builder.append(images[i][0]);
            }
            this.borders.put(builder.toString(), -1);

            // Right border from top to bottom
            builder = new StringBuilder();
            for (int i = 0; i <= maxI; i++) {
                builder.append(images[i][maxI]);
            }
            this.borders.put(builder.toString(), -1);

            return this;
        }
    }

    public static long calculate(Map<Integer, Tile> tiles) {
        tiles.values().forEach(Tile::setBorders);

        Map<String, List<Integer>> borders = new HashMap<>();

        // Find matching borders from the tiles
        tiles.forEach((tileId, tile) -> tile.borders.keySet().forEach(border -> {
            if (borders.containsKey(border)) {
                borders.get(border).add(tile.id);
                return;
            }

            String borderFlipped = new StringBuilder(border).reverse().toString();
            if (borders.containsKey(borderFlipped)) {
                borders.get(borderFlipped).add(tile.id);
                return;
            }

            List<Integer> ids = new LinkedList<>();
            ids.add(tile.id);
            borders.put(border, ids);
        }));

        // Updating tiles based on matching borders
        borders.forEach((border, tileIds) -> {
            if (tileIds.size() == 1) {
                return;
            }

            Integer tileId1 = tileIds.get(0);
            Integer tileId2 = tileIds.get(1);

            Tile tile1 = tiles.get(tileId1);
            Tile tile2 = tiles.get(tileId2);

            tile1.borders.put(border, tileId2);

            if (tile2.borders.containsKey(border)) {
                tile2.borders.put(border, tileId1);
                return;
            }
            String borderFlipped = new StringBuilder(border).reverse().toString();
            tile2.borders.put(borderFlipped, tileId1);
        });

        // If a tile has two unmatched borders, then it's a corner tile
        Set<Integer> cornerIds = tiles.entrySet().stream()
                .filter(entry -> {
                    Tile tile = entry.getValue();

                    long count = tile.borders.values().stream().filter(i -> i == -1).count();
                    return count == 2;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        long product = 1L;
        for (int corner : cornerIds) {
            product *= corner;
        }
        return product;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, Tile> tiles = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = calculate(tiles);
        System.out.println(result);
    }

    private static Map<Integer, Tile> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Map<Integer, Tile> tiles = new HashMap<>();

            while (scanner.hasNext()) {
                List<char[]> images = new ArrayList<>();

                String line = scanner.nextLine();

                Matcher matcher = PATTERN_ID.matcher(line);
                if (!matcher.matches()) {
                    continue;
                }
                int id = Integer.parseInt(matcher.group(1));

                line = scanner.nextLine();
                while (!line.isEmpty()) {
                    images.add(line.toCharArray());

                    if (scanner.hasNext()) {
                        line = scanner.nextLine();
                    } else {
                        line = "";
                    }
                }

                tiles.put(id, new Tile(id, images.toArray(new char[0][])));
            }
            return tiles;
        }
    }
}
