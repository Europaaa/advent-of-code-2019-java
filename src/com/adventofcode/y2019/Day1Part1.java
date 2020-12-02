package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1Part1 {

    private static final String INPUT_FILE = "y2019/day1";

    public static long calculateFuel(List<Long> mass) {
        long fuel = 0;
        for (Long m : mass) {
            fuel += m / 3 - 2;
        }
        return fuel;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Long> mass = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long fuel = calculateFuel(mass);
        System.out.println(fuel);
    }

    private static List<Long> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Long> mass = new ArrayList<>();
            while (scanner.hasNext()) {
                mass.add(scanner.nextLong());
            }
            return mass;
        }
    }
}
