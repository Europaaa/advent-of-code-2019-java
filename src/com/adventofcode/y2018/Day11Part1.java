package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day11Part1 {
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
        int x = 0, y = 0;
        for (int iX = 1; iX <= SIZE - 3; iX++) {
            for (int iY = 1; iY <= SIZE - 3; iY++) {
                long sum = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        sum += power[iY + j][iX + i];
                    }
                }

                if (sum > max) {
                    max = sum;
                    x = iX;
                    y = iY;
                }
            }
        }
        return x + "," + y;
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
