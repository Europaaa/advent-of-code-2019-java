package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day12Part2 {

    private static final String INPUT_FILE = "y2020/day12";

    private static final int WP_X = 10;
    private static final int WP_Y = 1;

    private long x;
    private long y;

    private long wpX;
    private long wpY;

    public Day12Part2() {
        this.x = 0;
        this.y = 0;

        this.wpX = WP_X;
        this.wpY = WP_Y;
    }

    public void move(String instruction) {
        char command = instruction.charAt(0);
        int value = Integer.parseInt(instruction.substring(1));

        if (command == 'R') {
            command = 'L';
            value = 360 - value;
        }

        switch (command) {
            case 'N':
                wpY += value;
                break;
            case 'S':
                wpY -= value;
                break;
            case 'E':
                wpX += value;
                break;
            case 'W':
                wpX -= value;
                break;
            case 'F':
                x += value * wpX;
                y += value * wpY;
                break;
            case 'L':
                long tempWpX = wpX;
                long tempWpY = wpY;

                switch (value) {
                    case 90:
                        this.wpX = -tempWpY;
                        this.wpY = tempWpX;
                        break;
                    case 180:
                        this.wpX = -this.wpX;
                        this.wpY = -this.wpY;
                        break;
                    case 270:
                        this.wpX = tempWpY;
                        this.wpY = -tempWpX;
                        break;
                    case 360:
                        break;
                }
                break;
        }
    }

    private long[] getLocation() {
        return new long[]{x, y};
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[] instructions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Day12Part2 day11Part1 = new Day12Part2();
        Arrays.stream(instructions).forEach(day11Part1::move);

        long[] location = day11Part1.getLocation();
        System.out.println(Math.abs(location[0]) + Math.abs(location[1]));
    }

    private static String[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> seats = new ArrayList<>();
            while (scanner.hasNext()) {
                seats.add(scanner.nextLine());
            }
            return seats.toArray(new String[0]);
        }
    }
}
