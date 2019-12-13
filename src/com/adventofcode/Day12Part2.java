package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Day12Part2 {

    private static final String INPUT_FILE = "day12";

    private static long getCycleSteps(Moon[] moons) {
        int n = moons[0].coords.length;

        long[] counters = new long[n];
        Arrays.fill(counters, 0l);

        HashSet[] snapshots = Collections.nCopies(n, new HashSet()).toArray(new HashSet[0]);

        for (int i = 0; Arrays.stream(counters).anyMatch(j -> j == 0); i++) {
            applyMotion(moons);

            for (int d = 0; d < n; d++) {
                String snapshot = getSnapshot(moons, d);
                if (!snapshots[d].add(snapshot) && counters[d] == 0) {
                    counters[d] = i;
                }
            }
        }

        // Get least common multiple of the three counters
        return lcm(counters);
    }

    private static long lcm(long[] numbers) {
        long result = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            result = lcm(result, numbers[i]);
        }
        return result;
    }

    private static long lcm(long n1, long n2) {
        return n1 * n2 / gcd(n1, n2);
    }

    private static long gcd(long n1, long n2) {
        while (n2 > 0) {
            long temp = n2;
            n2 = n1 % n2;
            n1 = temp;
        }
        return n1;
    }

    private static String getSnapshot(Moon[] moons, int dimension) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < moons.length; i++) {
            Moon moon = moons[i];
            builder.append("c").append(moon.coords[dimension]).append("v").append(moon.velocity[dimension]);
        }
        return builder.toString();
    }

    private static void applyMotion(Moon[] moons) {
        for (int i = 0; i < moons.length; i++) {
            for (int j = i + 1; j < moons.length; j++) {
                applyGravity(moons[i], moons[j]);
            }
        }

        for (int i = 0; i < moons.length; i++) {
            applyVelocity(moons[i]);
        }
    }

    private static void applyGravity(Moon moon1, Moon moon2) {
        for (int i = 0; i < moon1.coords.length; i++) {
            if (moon1.coords[i] < moon2.coords[i]) {
                moon1.velocity[i]++;
                moon2.velocity[i]--;
            } else if (moon1.coords[i] > moon2.coords[i]) {
                moon1.velocity[i]--;
                moon2.velocity[i]++;
            }
        }
    }

    private static void applyVelocity(Moon moon) {
        for (int i = 0; i < moon.coords.length; i++) {
            moon.coords[i] += moon.velocity[i];
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Moon[] moons = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(getCycleSteps(moons));
    }

    static class Moon {
        long[] coords;
        long[] velocity;

        Moon(long[] coords) {
            this.coords = coords;
            this.velocity = new long[coords.length];
        }
    }

    private static Moon[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Moon[] moons = new Moon[4];
            for (int i = 0; i < moons.length; i++) {
                String line = scanner.nextLine();
                line = line.substring(1, line.length() - 1);

                String[] tokens = line.split(",");

                long[] coords = new long[tokens.length];
                for (int j = 0; j < tokens.length; j++) {
                    String value = tokens[j].split("=")[1];
                    coords[j] = Long.valueOf(value);
                }

                moons[i] = new Moon(coords);
            }
            return moons;
        }
    }
}
