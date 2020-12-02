package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day5Part2 {

    private static final String INPUT_FILE = "y2020/day5";

    private static final int MAX_ROW = 127;
    private static final int MAX_COLUMN = 7;

    public static long findMySeat(List<String> seats) {
        long[] seatIds = seats.stream().mapToLong(Day5Part2::getSeatId).sorted().toArray();

        long minSeatId = seatIds[0];
        for (int i = 0; i < seatIds.length; i++) {
            long seatId = minSeatId + i;
            if (seatIds[i] != seatId) {
                return seatId;
            }
        }
        return -1;
    }

    private static long getSeatId(String seat) {
        char[] chars = seat.toCharArray();

        int rowMin = 0, rowMax = MAX_ROW, columnMin = 0, columnMax = MAX_COLUMN;
        for (int i = 0; i < 7; i++) {
            int rowMid = rowMin + (rowMax - rowMin) / 2;
            switch (chars[i]) {
                case 'F':
                    rowMax = rowMid;
                    break;
                case 'B':
                    rowMin = rowMid + 1;
                    break;
            }
        }

        for (int i = 7; i < 10; i++) {
            int columnMid = columnMin + (columnMax - columnMin) / 2;
            switch (chars[i]) {
                case 'L':
                    columnMax = columnMid;
                    break;
                case 'R':
                    columnMin = columnMid + 1;
                    break;
            }
        }
        return ((long) rowMin) * 8 + columnMin;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> seats = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long seatId = findMySeat(seats);
        System.out.println(seatId);
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> seats = new ArrayList<>();
            while (scanner.hasNext()) {
                seats.add(scanner.nextLine().trim());
            }
            return seats;
        }
    }
}
