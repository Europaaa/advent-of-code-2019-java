package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day11Part2 {

    private static final String INPUT_FILE = "y2020/day11";

    private char[][] seats;

    private boolean[][] ups;
    private boolean[][] downs;
    private boolean[][] lefts;
    private boolean[][] rights;
    private boolean[][] upLefts;
    private boolean[][] upRights;
    private boolean[][] downLefts;
    private boolean[][] downRights;

    public Day11Part2(char[][] seats) {
        this.seats = seats;

        int r = seats.length;
        int c = seats[0].length;

        this.ups = new boolean[r][c];
        this.downs = new boolean[r][c];
        this.lefts = new boolean[r][c];
        this.rights = new boolean[r][c];
        this.upLefts = new boolean[r][c];
        this.upRights = new boolean[r][c];
        this.downLefts = new boolean[r][c];
        this.downRights = new boolean[r][c];
    }

    public void stabilize() {
        int i = 0;
        do {
            calculate();
        } while (update());
    }

    private void calculate() {
        for (int c = 0; c < seats[0].length; c++) {
            this.ups[0][c] = false;
            this.ups[1][c] = seats[0][c] == '#';
        }
        for (int r = 2; r < seats.length; r++) {
            for (int c = 0; c < seats[0].length; c++) {
                char seat = seats[r - 1][c];
                this.ups[r][c] = seat == '.' ? this.ups[r - 1][c] : seat == '#';
            }
        }

        for (int c = 0; c < seats[0].length; c++) {
            this.downs[seats.length - 1][c] = false;
            this.downs[seats.length - 2][c] = seats[seats.length - 1][c] == '#';
        }
        for (int r = seats.length - 2; r >= 0; r--) {
            for (int c = 0; c < seats[0].length; c++) {
                char seat = seats[r + 1][c];
                this.downs[r][c] = seat == '.' ? this.downs[r + 1][c] : seat == '#';
            }
        }

        for (int r = 0; r < seats.length; r++) {
            this.lefts[r][0] = false;
            this.lefts[r][1] = this.seats[r][0] == '#';
        }
        for (int r = 0; r < seats.length; r++) {
            for (int c = 2; c < seats[0].length; c++) {
                char seat = seats[r][c - 1];
                this.lefts[r][c] = seat == '.' ? this.lefts[r][c - 1] : seat == '#';
            }
        }

        for (int r = 0; r < seats.length; r++) {
            this.rights[r][seats[0].length - 1] = false;
            this.rights[r][seats[0].length - 2] = this.seats[r][seats[0].length - 1] == '#';
        }
        for (int r = 0; r < seats.length; r++) {
            for (int c = seats[0].length - 2; c >= 0; c--) {
                char seat = seats[r][c + 1];
                this.rights[r][c] = seat == '.' ? this.rights[r][c + 1] : seat == '#';
            }
        }

        for (int r = 0; r < seats.length; r++) {
            this.upLefts[r][0] = false;
        }
        for (int c = 0; c < seats[0].length; c++) {
            this.upLefts[0][c] = false;
        }
        for (int r = 1; r < seats.length; r++) {
            for (int c = 1; c < seats[0].length; c++) {
                char seat = seats[r - 1][c - 1];
                this.upLefts[r][c] = seat == '.' ? this.upLefts[r - 1][c - 1] : seat == '#';
            }
        }

        for (int r = 0; r < seats.length; r++) {
            this.upRights[r][seats[0].length - 1] = false;
        }
        for (int c = 0; c < seats[0].length; c++) {
            this.upRights[0][c] = false;
        }
        for (int r = 1; r < seats.length; r++) {
            for (int c = seats[0].length - 2; c >= 0; c--) {
                char seat = seats[r - 1][c + 1];
                this.upRights[r][c] = seat == '.' ? this.upRights[r - 1][c + 1] : seat == '#';
            }
        }

        for (int r = 0; r < seats.length; r++) {
            this.downLefts[r][0] = false;
        }
        for (int c = 0; c < seats[0].length; c++) {
            this.downLefts[seats.length - 1][c] = false;
        }
        for (int r = seats.length - 2; r >= 0; r--) {
            for (int c = 1; c < seats[0].length; c++) {
                char seat = seats[r + 1][c - 1];
                this.downLefts[r][c] = seat == '.' ? this.downLefts[r + 1][c - 1] : seat == '#';
            }
        }

        for (int r = 0; r < seats.length; r++) {
            this.downRights[r][seats[0].length - 1] = false;
        }
        for (int c = 0; c < seats[0].length; c++) {
            this.downRights[seats.length - 1][c] = false;
        }
        for (int r = seats.length - 2; r >= 0; r--) {
            for (int c = seats[0].length - 2; c >= 0; c--) {
                char seat = seats[r + 1][c + 1];
                this.downRights[r][c] = seat == '.' ? this.downRights[r + 1][c + 1] : seat == '#';
            }
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
                        if (getOccupiedCount(r, c) >= 5) {
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

        if (this.ups[r][c]) {
            count++;
        }
        if (this.downs[r][c]) {
            count++;
        }
        if (this.lefts[r][c]) {
            count++;
        }
        if (this.rights[r][c]) {
            count++;
        }
        if (this.upLefts[r][c]) {
            count++;
        }
        if (this.upRights[r][c]) {
            count++;
        }
        if (this.downLefts[r][c]) {
            count++;
        }
        if (this.downRights[r][c]) {
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[][] seats = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Day11Part2 day11Part1 = new Day11Part2(seats);
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
