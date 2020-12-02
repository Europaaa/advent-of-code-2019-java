package com.adventofcode.y2015;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Day3Part1 {

    private static final String INPUT_FILE = "y2015/day3";

    public static long evaluate(char[] codes) {
        Map<Coordinate, Long> visited = new HashMap<>();

        long x = 0;
        long y = 0;

        Coordinate coordinate = new Coordinate(x, y);
        visited.put(coordinate, 1l);

        for (char code : codes) {
            switch (code) {
                case '>':
                    x += 1;
                    break;
                case '<':
                    x -= 1;
                    break;
                case '^':
                    y -= 1;
                    break;
                case 'v':
                    y += 1;
                    break;
            }

            coordinate = new Coordinate(x, y);
            if (!visited.containsKey(coordinate)) {
                visited.put(coordinate, visited.getOrDefault(coordinate, 0l) + 1l);
            }
        }
        return visited.values().stream().filter(i -> i >= 1l).count();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String codes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(codes.toCharArray()));
    }

    private static String getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextLine();
        }
    }

    private static class Coordinate {
        private long x;
        private long y;

        private Coordinate(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
