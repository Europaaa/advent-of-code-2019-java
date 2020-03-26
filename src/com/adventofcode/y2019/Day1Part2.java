package com.adventofcode.y2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1Part2 {

    private static final String INPUT_FILE = "y2019/day1";

    public static long calculateFuel(List<Long> mass) {
        long fuel = 0;
        for (Long m : mass) {
            fuel += calculateFuel(m);
        }
        return fuel;
    }

    private static long calculateFuel(Long mass) {
        long totalFuel = -mass;
        long fuel = mass;

        do {
            totalFuel += fuel;
            fuel = fuel / 3 - 2;
        } while (fuel > 0);

        return totalFuel;
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
