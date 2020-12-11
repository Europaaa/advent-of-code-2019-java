package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day11Part1 {

    private static final String INPUT_FILE = "y2020/day11";

    private char[][] seats;

    public Day11Part1(char[][] seats) {
        this.seats = seats;
    }

    public void stabilize() {
        while (update()) {
        }
    }

    private boolean update() {
        boolean updated = false;

        char[][] seatsUpdated = Arrays.stream(seats).map(char[]::clone).toArray(char[][]::new);
        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[0].length; c++) {
                char seat = seats[r][c];
                switch (seat) {
                    case 'L':
                        if (getOccupiedCount(r, c) == 0) {
                            seatsUpdated[r][c] = '#';
                            updated = true;
                        }
                        break;
                    case '#':
                        if (getOccupiedCount(r, c) > 4) {
                            seatsUpdated[r][c] = 'L';
                            updated = true;
                        }
                        break;
                    case '.':
                        break;
                }
            }
        }

        this.seats = seatsUpdated;
        return updated;
    }

    private int getOccupiedCount(int r, int c) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            if (r + i < 0 || r + i >= seats.length) {
                continue;
            }

            for (int j = -1; j <= 1; j++) {
                if (c + j < 0 || c + j >= seats[0].length) {
                    continue;
                }
                if (seats[r + i][c + j] == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[][] seats = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Day11Part1 day11Part1 = new Day11Part1(seats);
        day11Part1.stabilize();

        int count = 0;
        for (int r = 0; r < day11Part1.seats.length; r++) {
            for (int c = 0; c < day11Part1.seats[0].length; c++) {
                if (day11Part1.seats[r][c] == '#') {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    private static char[][] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<char[]> seats = new ArrayList<>();
            while (scanner.hasNext()) {
                seats.add(scanner.nextLine().toCharArray());
            }
            return seats.toArray(new char[0][]);
        }
    }
}
