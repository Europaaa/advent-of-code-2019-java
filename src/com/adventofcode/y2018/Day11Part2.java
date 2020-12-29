package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day11Part2 {
    private static final String INPUT_FILE = "y2018/day11";

    private static final int SIZE = 300;

    public static String calculate(int serial) {
        long[][] power = new long[SIZE + 1][SIZE + 1];
        Arrays.stream(power).forEach(row -> Arrays.fill(row, 0));

        for (int y = 1; y <= SIZE; y++) {
            for (int x = 1; x < SIZE; x++) {
                power[y][x] = getPower(x, y, serial);
            }
        }

        long max = 0;
        int x = 0, y = 0, size = 1;

        long[][] memo = new long[SIZE + 1][SIZE + 1];

        // By default, we assume the size of the max power level is reached when size = 1
        for (int iY = 1; iY <= SIZE; iY++) {
            for (int iX = 1; iX < SIZE; iX++) {
                memo[iY][iX] = power[iY][iX];

                if (memo[iY][iX] > max) {
                    max = memo[iY][iX];

                    x = iX;
                    y = iY;
                    size = 1;
                }
            }
        }

        for (int s = 2; s <= SIZE; s++) {
            for (int iY = 1; iY <= SIZE - s; iY++) {
                for (int iX = 1; iX < SIZE - s; iX++) {
                    memo[iY][iX] = memo[iY][iX];
                    for (int i = 0; i < s; i++) {
                        memo[iY][iX] += power[iY + i][iX + s - 1];
                    }
                    for (int i = 0; i < s; i++) {
                        memo[iY][iX] += power[iY + s - 1][iX + i];
                    }

                    if (memo[iY][iX] > max) {
                        max = memo[iY][iX];

                        x = iX;
                        y = iY;
                        size = s;
                    }
                }
            }
        }
        return x + "," + y + "," + size;
    }

    private static long getPower(int x, int y, int serial) {
        int rackId = x + 10;

        int power = (rackId * y + serial) * rackId;
        return power / 100 - (power / 1000) * 10 - 5;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int serial = getInput(CommonUtils.getInputFile(INPUT_FILE));

        String coordinate = calculate(serial);
        System.out.println(coordinate);
    }

    private static int getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextInt();
        }
    }
}
