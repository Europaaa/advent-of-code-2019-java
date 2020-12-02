package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Day5Part1 {

    private static final String INPUT_FILE = "y2020/day5";

    private static final int MAX_ROW = 127;
    private static final int MAX_COLUMN = 7;

    public static long getSeatId(String seat) {
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

        Optional<Long> result = seats.stream().map(Day5Part1::getSeatId).max(Comparator.naturalOrder());
        System.out.println(result.orElse(-1L));
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
