package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13Part1 {

    private static final String INPUT_FILE = "y2020/day13";

    private long timestamp;
    private Set<Integer> buses;

    public Day13Part1() {
    }

    public long[] getResult() {
        for (long t = timestamp; ; t++) {
            for (Integer bus : buses) {
                if (t % bus == 0) {
                    return new long[]{bus, t - timestamp};
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day13Part1 day13Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long[] result = day13Part1.getResult();
        System.out.println(result[0] * result[1]);
    }

    private static Day13Part1 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Day13Part1 day13Part1 = new Day13Part1();
            day13Part1.timestamp = Long.parseLong(scanner.nextLine());

            String bus = scanner.nextLine();
            day13Part1.buses = Arrays.stream(bus.split(","))
                    .filter(b -> !b.equals("x"))
                    .map(b -> Integer.parseInt(b))
                    .collect(Collectors.toSet());
            return day13Part1;
        }
    }
}
