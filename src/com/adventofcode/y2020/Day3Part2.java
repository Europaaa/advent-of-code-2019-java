package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3Part2 {

    private static final String INPUT_FILE = "y2020/day3";

    public static long calculate(char[][] map, int deltaX, int deltaY) {
        int n = map[0].length;
        int x = 0, y = 0;

        long count = 0;
        while (y < map.length) {
            x = (x + deltaX) % n;
            y = y + deltaY;

            if (y < map.length && map[y][x] == '#') {
                count += 1;
            }
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[][] map = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = calculate(map, 1, 1)
                * calculate(map, 3, 1)
                * calculate(map, 5, 1)
                * calculate(map, 7, 1)
                * calculate(map, 1, 2);

        System.out.println(result);
    }

    private static char[][] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<char[]> expenses = new ArrayList<>();
            while (scanner.hasNext()) {
                expenses.add(scanner.nextLine().trim().toCharArray());
            }
            return expenses.toArray(new char[0][]);
        }
    }
}
