package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day12Part1 {

    private static final String INPUT_FILE = "y2020/day12";
    private static final Direction[] DIRECTIONS = Direction.values();

    enum Direction {
        NORTH(0, 1),
        WEST(-1, 0),
        SOUTH(0, -1),
        EAST(1, 0);

        private int dirX;
        private int dirY;

        Direction(int dirX, int dirY) {
            this.dirX = dirX;
            this.dirY = dirY;
        }
    }

    private long x;
    private long y;
    private int iDirection;

    public Day12Part1() {
        this.x = 0;
        this.y = 0;
        this.iDirection = Direction.EAST.ordinal();
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
                y += value;
                break;
            case 'S':
                y -= value;
                break;
            case 'E':
                x += value;
                break;
            case 'W':
                x -= value;
                break;
            case 'F':
                Direction direction = DIRECTIONS[iDirection];
                x += (direction.dirX) * value;
                y += (direction.dirY) * value;
                break;
            case 'L':
                iDirection = (iDirection + value / 90) % (DIRECTIONS.length);
                break;
        }
    }

    private long[] getLocation() {
        return new long[]{x, y};
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[] instructions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Day12Part1 day11Part1 = new Day12Part1();
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
