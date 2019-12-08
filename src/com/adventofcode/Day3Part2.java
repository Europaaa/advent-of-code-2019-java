package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3Part2 {

    private static final String INPUT_FILE = "day3";

    public static int getMinStepsToIntersection(String[] path1, String[] path2) {
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

        int[] distances1 = getDistances(new int[]{maxLeft, maxUp}, path1, intersections);
        int[] distances2 = getDistances(new int[]{maxLeft, maxUp}, path2, intersections);

        return IntStream.range(0, intersections.size())
                .map(i -> distances1[i] + distances2[i])
                .filter(distance -> distance > 0)
                .min().getAsInt();
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

    private static int[] getDistances(int[] center, String[] path, List<int[]> intersections) {
        List<String> hashes = intersections.stream().map(Day3Part2::getHash).collect(Collectors.toList());

        int[] distances = new int[intersections.size()];

        int x = center[0];
        int y = center[1];

        int steps = 0;
        for (int i = 0; i < path.length; i++) {
            String step = path[i];

            char direction = step.charAt(0);
            int count = Integer.valueOf(step.substring(1));

            for (int j = 0; j < count; j++) {
                switch (direction) {
                    case 'L':
                        x--;
                        break;
                    case 'R':
                        x++;
                        break;
                    case 'U':
                        y--;
                        break;
                    case 'D':
                        y++;
                        break;
                }
                steps++;

                String hash = getHash(new int[]{x, y});
                if (hashes.contains(hash)) {
                    distances[hashes.indexOf(hash)] = steps;
                }
            }
        }
        return distances;
    }

    private static String getHash(int[] coords) {
        return coords[0] + ":" + coords[1];
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[][] paths = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int distance = getMinStepsToIntersection(paths[0], paths[1]);
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
