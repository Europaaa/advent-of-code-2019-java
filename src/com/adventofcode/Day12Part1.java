package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day12Part1 {

    private static final String INPUT_FILE = "day12";

    private static int getTotalEnergy(Moon[] moons, int target) {
        for (int step = 0; step < target; step++) {
            applyMotion(moons);
        }
        return Arrays.stream(moons).mapToInt(Day12Part1::getTotalEnery).sum();
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

    private static int getTotalEnery(Moon moon) {
        int potentialEnergy = Arrays.stream(moon.coords).map(Math::abs).sum();
        int kineticEnergy = Arrays.stream(moon.velocity).map(Math::abs).sum();
        return potentialEnergy * kineticEnergy;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Moon[] moons = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(getTotalEnergy(moons, 1000));
    }

    static class Moon {
        int[] coords;
        int[] velocity;

        Moon(int[] coords) {
            this.coords = coords;
            this.velocity = new int[coords.length];
        }
    }

    private static Moon[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Moon[] moons = new Moon[4];
            for (int i = 0; i < moons.length; i++) {
                String line = scanner.nextLine();
                line = line.substring(1, line.length() - 1);

                String[] tokens = line.split(",");

                int[] coords = new int[tokens.length];
                for (int j = 0; j < tokens.length; j++) {
                    String value = tokens[j].split("=")[1];
                    coords[j] = Integer.valueOf(value);
                }

                moons[i] = new Moon(coords);
            }
            return moons;
        }
    }
}
