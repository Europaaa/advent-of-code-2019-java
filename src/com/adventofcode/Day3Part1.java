package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3Part1 {

    private static final String INPUT_FILE = "day3";

    public static int getDistanceToClosestIntersection(String[] path1, String[] path2) {
        int[] metrics = getMetrics(path1, new int[]{0, 0, 0, 0});
        metrics = getMetrics(path2, metrics);

        int maxLeft = metrics[0];
        int maxRight = metrics[1];
        int maxUp = metrics[2];
        int maxDown = metrics[3];

        boolean[][] trace1 = new boolean[maxUp + maxDown + 1][maxLeft + maxRight + 1];
        trace(path1, trace1, maxLeft, maxUp);

        boolean[][] trace2 = new boolean[maxUp + maxDown + 1][maxLeft + maxRight + 1];
        trace(path2, trace2, maxLeft, maxUp);

        List<int[]> intersections = findIntersections(trace1, trace2);
        return getMinDistance(new int[]{maxLeft, maxUp}, intersections);
    }

    private static int[] getMetrics(String[] path1, int[] metrics) {
        int maxLeft = metrics[0];
        int maxRight = metrics[1];
        int maxUp = metrics[2];
        int maxDown = metrics[3];

        int x = 0;
        int y = 0;

        for (String step : path1) {
            char direction = step.charAt(0);
            int count = Integer.valueOf(step.substring(1));

            switch (direction) {
                case 'L':
                    x -= count;
                    maxLeft = Math.max(maxLeft, -x);
                    break;
                case 'R':
                    x += count;
                    maxRight = Math.max(maxRight, x);
                    break;
                case 'U':
                    y += count;
                    maxUp = Math.max(maxUp, y);
                    break;
                case 'D':
                    y -= count;
                    maxDown = Math.max(maxDown, -y);
                    break;
            }
        }

        return new int[]{maxLeft, maxRight, maxUp, maxDown};
    }

    private static void trace(String[] path, boolean[][] trace, int x, int y) {
        for (String step : path) {
            char direction = step.charAt(0);
            int count = Integer.valueOf(step.substring(1));

            switch (direction) {
                case 'L':
                    for (int i = 1; i <= count; i++) {
                        trace[y][x--] = true;
                    }
                    break;
                case 'R':
                    for (int i = 1; i <= count; i++) {
                        trace[y][x++] = true;
                    }
                    break;
                case 'U':
                    for (int i = 1; i <= count; i++) {
                        trace[y--][x] = true;
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= count; i++) {
                        trace[y++][x] = true;
                    }
                    break;
            }
        }
    }

    private static List<int[]> findIntersections(boolean[][] trace1, boolean[][] trace2) {
        List<int[]> intersections = new ArrayList<>();
        for (int y = 0; y < trace1.length; y++) {
            for (int x = 0; x < trace1[y].length; x++) {
                if (trace1[y][x] && trace2[y][x]) {
                    intersections.add(new int[]{x, y});
                }
            }
        }
        return intersections;
    }

    private static int getMinDistance(int[] center, List<int[]> intersections) {
        int minDistance = Integer.MAX_VALUE;
        for (int[] coords : intersections) {
            if (center[0] == coords[0] && center[1] == coords[1]) {
                continue;
            }
            minDistance = Math.min(minDistance, Math.abs(center[0] - coords[0]) + Math.abs(center[1] - coords[1]));
        }
        return minDistance;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[][] paths = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int distance = getDistanceToClosestIntersection(paths[0], paths[1]);
        System.out.println(distance);
    }

    private static String[][] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            String[] path1 = scanner.nextLine().split(",");
            String[] path2 = scanner.nextLine().split(",");
            return new String[][]{path1, path2};
        }
    }
}
