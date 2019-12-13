package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day10Part2 {

    private static final String INPUT_FILE = "day10";

    private static final char CELL_EMPTY = '.';

    public static int[] getCoordsForTarget(char[][] map, int target) {
        int[] station = getBestLocation(map);

        Map<BigDecimal, List<int[]>> angels = getAngels(map, station[0], station[1]);

        int iteration = 0;
        int count = 0;
        int c = 0;

        do {
            iteration++;
            count += c;

            // How many angels has more than i number of asteroids on it
            int i = iteration;
            c = (int) angels.keySet().stream()
                    .filter(key -> angels.get(key).size() >= i)
                    .count();
        } while (count + c < target);

        // Calculate in this iteration what is the order of targets
        int i = iteration;
        List<BigDecimal> angelsSorted = angels.keySet().stream()
                .filter(key -> angels.get(key).size() >= i)
                .sorted()
                .collect(Collectors.toList());

//        int index = count + c - target;
        int index = target - count;
        List<int[]> coords = angels.get(angelsSorted.get(index - 1));
        coords.sort((coords1, coords2) -> {
            double distance1 = getDistance(coords1[0], coords1[1], station[0], station[1]);
            double distance2 = getDistance(coords2[0], coords2[1], station[0], station[1]);
            return Double.compare(distance1, distance2);
        });

        return coords.get(iteration - 1);
    }

    private static int[] getBestLocation(char[][] map) {
        int maxCount = 0;
        int[] bestLocation = null;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == CELL_EMPTY) {
                    continue;
                }
                int count = getAngels(map, j, i).size();
                if (maxCount < count) {
                    maxCount = count;
                    bestLocation = new int[]{j, i};
                }
            }
        }
        return bestLocation;
    }

    private static Map<BigDecimal, List<int[]>> getAngels(char[][] map, int x, int y) {
        Map<BigDecimal, List<int[]>> angels = new HashMap<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (x == j && y == i) {
                    continue;
                }
                if (map[i][j] == CELL_EMPTY) {
                    continue;
                }

                BigDecimal angel = BigDecimal.valueOf(getAngel(x, y, j, i));
                List<int[]> coords = angels.getOrDefault(angel, new ArrayList<>());
                coords.add(new int[]{j, i});
                angels.put(angel, coords);
            }
        }
        return angels;
    }

    private static double getAngel(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Adjust the angle so that it's 0 degree when it's pointing straight up
        angle = -angle - 180;
        // Keep angle between 0 and 360
        return angle + Math.ceil(-angle / 360) * 360;
    }

    private static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[][] map = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int[] coords = getCoordsForTarget(map, 200);
        System.out.println(coords[0] * 100 + coords[1]);
    }

    private static char[][] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<char[]> map = new ArrayList<>();
            while (scanner.hasNext()) {
                char[] chars = scanner.nextLine().toCharArray();
                map.add(chars);
            }
            return map.toArray(new char[0][]);
        }
    }
}
