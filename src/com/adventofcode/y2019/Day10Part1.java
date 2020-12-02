package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day10Part1 {

    private static final String INPUT_FILE = "y2019/day10";

    private static final char CELL_EMPTY = '.';

    public static int getCountFromBestView(char[][] map) {
        int maxCount = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == CELL_EMPTY) {
                    continue;
                }
                maxCount = Math.max(maxCount, getCountForCoords(map, j, i));
            }
        }
        return maxCount;
    }

    private static int getCountForCoords(char[][] map, int x, int y) {
        Set<BigDecimal> angels = new HashSet<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (x == j && y == i) {
                    continue;
                }
                if (map[i][j] == CELL_EMPTY) {
                    continue;
                }
                angels.add(BigDecimal.valueOf(getAngel(x, y, j, i)));
            }
        }
        return angels.size();
    }

    private static double getAngel(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        return angle + Math.ceil(-angle / 360) * 360;
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[][] map = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int count = getCountFromBestView(map);
        System.out.println(count);
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
