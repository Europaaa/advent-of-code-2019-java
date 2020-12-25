package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day20Part2 {

    private static final String INPUT_FILE = "y2020/day20";

    private static final Pattern PATTERN_ID = Pattern.compile("Tile\\s(\\d+):");
    private static final char[][] SEA_MONSTER = new char[][]{
            "                  # ".toCharArray(),
            "#    ##    ##    ###".toCharArray(),
            " #  #  #  #  #  #   ".toCharArray()
    };

    enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }

    private static class Tile {
        private int id;
        private char[][] images;

        // Stores matching tile ids for a specific border
        private Map<String, Integer> borders;

        public Tile(int id, char[][] images) {
            this.id = id;
            this.images = images;

            this.borders = new HashMap<>();
            Arrays.stream(Direction.values())
                    .forEach(direction -> this.borders.put(getBorder(direction), -1));
        }

        private String getBorder(Direction direction) {
            int n = images.length;

            switch (direction) {
                case TOP:
                    return new String(images[0]);
                case BOTTOM:
                    return reverse(new String(images[n - 1]));
                case RIGHT:
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < n; i++) {
                        builder.append(images[i][n - 1]);
                    }
                    return builder.toString();
                case LEFT:
                    builder = new StringBuilder();
                    for (int i = 0; i < n; i++) {
                        builder.append(images[n - i - 1][0]);
                    }
                    return builder.toString();
            }
            throw new IllegalStateException();
        }

        /**
         * Rotate clockwise direction for the specified degrees (e.g. 90, 180, 270)
         **/
        private Tile rotate(int degree) {
            int step = (degree / 90) % 4;
            for (int i = 0; i < step; i++) {
                this.images = rotateImage(this.images);
            }
            return this;
        }

        /**
         * Flip the tile along the x axis
         */
        private Tile flipX() {
            Map<String, Integer> newBorders = new HashMap<>();
            Arrays.stream(Direction.values())
                    .forEach(direction -> {
                        String border = this.getBorder(direction);
                        newBorders.put(reverse(border), borders.get(border));
                    });

            this.borders = newBorders;

            int n = images.length;

            char[][] newImage = new char[n][n];
            for (int i = 0; i < newImage.length; i++) {
                newImage[i] = images[n - i - 1];
            }
            this.images = newImage;

            return this;
        }

        private Tile flipY() {
            Map<String, Integer> newBorders = new HashMap<>();
            Arrays.stream(Direction.values())
                    .forEach(direction -> {
                        String border = this.getBorder(direction);
                        newBorders.put(reverse(border), borders.get(border));
                    });

            this.borders = newBorders;

            int n = images.length;

            char[][] newImage = new char[n][n];
            for (int i = 0; i < newImage.length; i++) {
                newImage[i] = reverse(new String(images[i])).toCharArray();
            }
            this.images = newImage;

            return this;
        }

        public void rotate(Direction direction, String border) {
            // Rotate the tile so that TOP edge matches
            String flippedBorder = reverse(border);

            Direction borderDirection = Arrays.stream(Direction.values())
                    .filter(dir -> {
                        String b = this.getBorder(dir);
                        return b.equals(border) || b.equals(flippedBorder);
                    })
                    .findFirst()
                    .get();

            int step = ((direction.ordinal() - borderDirection.ordinal() + 4) % 4);
            this.rotate(step * 90);

            // If the tile doesn't have a match for the target border, it means we will need to flip it
            if (this.borders.containsKey(border)) {
                return;
            }

            switch (direction) {
                case LEFT:
                case RIGHT:
                    this.flipX();
                    return;
                default:
                    this.flipY();
            }
        }

        private void flip(Direction direction, String border) {
            Runnable flip;
            switch (direction) {
                case LEFT:
                case RIGHT:
                    flip = this::flipY;
                    break;
                default:
                    flip = this::flipX;
                    break;
            }

            if (border == null) {
                if (this.borders.get(this.getBorder(direction)) != -1) {
                    flip.run();
                }
            } else {
                if (this.getBorder(direction).equals(border)) {
                    flip.run();
                }
            }
        }
    }

    public static long calculate(Map<Integer, Tile> tiles) {
        char[][] image = assemble(tiles);

        char[][] pattern = SEA_MONSTER;
        boolean[][] marks = new boolean[image.length][image.length];

        for (int i = 0; i < 4; i++) {
            mark(image, pattern, marks);
            pattern = rotateImage(pattern);
        }

        long count = 0;
        for (int i = 0; i < marks.length; i++) {
            for (int j = 0; j < marks[i].length; j++) {
                if (!marks[i][j] && image[i][j] == '#') {
                    count += 1;
                }
            }
        }
        return count;
    }

    private static char[][] assemble(Map<Integer, Tile> tiles) {
        Map<String, List<Integer>> borders = new HashMap<>();

        // Find matching borders from the tiles
        tiles.forEach((tileId, tile) -> tile.borders.keySet().forEach(border -> {
            if (borders.containsKey(border)) {
                borders.get(border).add(tile.id);
                return;
            }

            String borderFlipped = reverse(border);
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
            String borderFlipped = reverse(border);
            tile2.borders.put(borderFlipped, tileId1);
        });

        // If a tile has two unmatched borders, then it's a corner tile
        List<Integer> cornerIds = tiles.entrySet().stream()
                .filter(entry -> {
                    Tile tile = entry.getValue();

                    long count = tile.borders.values().stream().filter(i -> i == -1).count();
                    return count == 2;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Setup for the assembled image
        int n = (int) Math.sqrt(tiles.size());

        int[][] ids = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(ids[i], -1);
        }

        // Pick any corner tile as top left corner
        ids[0][0] = cornerIds.get(0);

        Tile cornerTile = tiles.get(ids[0][0]);
        String border = cornerTile.borders.entrySet().stream()
                // Get a non-edge border
                .filter(entry -> entry.getValue() != -1)
                .map(Map.Entry::getKey)
                .findFirst()
                .get();

        cornerTile.rotate(Direction.RIGHT, border);
        cornerTile.flip(Direction.TOP, null);

        // Find adjacent tiles for the first row
        for (int i = 0; i < n - 1; i++) {
            Tile tile = tiles.get(ids[0][i]);

            border = tile.getBorder(Direction.RIGHT);
            ids[0][i + 1] = tile.borders.get(border);

            Tile adjacentTile = tiles.get(ids[0][i + 1]);
            adjacentTile.rotate(Direction.LEFT, reverse(border));
            adjacentTile.flip(Direction.TOP, null);
        }

        // Find adjacent tiles for the first column
        for (int i = 0; i < n - 1; i++) {
            Tile tile = tiles.get(ids[i][0]);

            border = tile.getBorder(Direction.BOTTOM);
            ids[i + 1][0] = tile.borders.get(border);

            Tile adjacentTile = tiles.get(ids[i + 1][0]);
            adjacentTile.rotate(Direction.TOP, reverse(border));
            adjacentTile.flip(Direction.LEFT, null);
        }

        // Find adjacent tiles for the rest of the image
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                Tile leftTile = tiles.get(ids[i][j - 1]);

                border = leftTile.getBorder(Direction.RIGHT);
                ids[i][j] = leftTile.borders.get(border);

                Tile tile = tiles.get(ids[i][j]);
                tile.rotate(Direction.LEFT, reverse(border));
            }
        }

        // Construct the images
        int s = tiles.get(ids[0][0]).images.length;
        int m = ids.length * (s - 2);

        char[][] images = new char[m][m];

        for (int i = 0; i < ids.length; i++) {
            for (int j = 0; j < ids[i].length; j++) {
                char[][] image = tiles.get(ids[i][j]).images;

                for (int r = 1; r < s - 1; r++) {
                    for (int c = 1; c < s - 1; c++) {
                        images[i * (s - 2) + (r - 1)][j * (s - 2) + (c - 1)] = image[r][c];
                    }
                }
            }
        }
        return images;
    }

    private static void mark(char[][] image, char[][] pattern, boolean[][] marks) {
        int n = image.length;

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (!matches(image, pattern, x, y)) {
                    continue;
                }

                for (int i = 0; i < pattern.length; i++) {
                    for (int j = 0; j < pattern[i].length; j++) {
                        char c = pattern[i][j];
                        if (c == '#') {
                            marks[y + i][x + j] = true;
                        }
                    }
                }
            }
        }
    }

    private static boolean matches(char[][] image, char[][] pattern, int x, int y) {
        int n = image.length;

        if (y + pattern.length >= n) {
            return false;
        }
        if (x + pattern[0].length >= n) {
            return false;
        }

        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                char c = pattern[i][j];
                if (c == ' ') {
                    continue;
                }
                if (image[y + i][x + j] != c) {
                    return false;
                }
            }
        }
        return true;
    }

    private static char[][] rotateImage(char[][] image) {
        int maxY = image.length;
        int maxX = image[0].length;

        char[][] newImage = new char[maxX][];
        for (int i = 0; i < maxX; i++) {
            newImage[i] = new char[maxY];
        }

        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                newImage[i][maxY - j - 1] = image[j][i];
            }
        }
        return newImage;
    }

    private static String reverse(String border) {
        return new StringBuilder(border).reverse().toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, Tile> tiles = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long count = calculate(tiles);
        System.out.println(count);
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
