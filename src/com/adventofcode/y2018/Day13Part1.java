package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day13Part1 {
    private static final String INPUT_FILE = "y2018/day13";

    private char[][] track;
    private List<Cart> carts;

    public Day13Part1(char[][] track, List<Cart> carts) {
        this.track = track;
        this.carts = carts;
    }

    public static class Cart {
        private int x;
        private int y;
        private Direction direction;

        private int counter;

        public Cart(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.counter = 0;
        }

        public Cart move(char[][] track) {
            this.x += direction.x;
            this.y += direction.y;

            char c = track[this.y][this.x];
            int ord = this.direction.ordinal();

            switch (c) {
                case '\\':
                    // Switch direction between UP and LEFT, or RIGHT and DOWN
                    this.direction = Direction.VALUES[3 - ord];
                    break;
                case '/':
                    // Switch direction between UP and RIGHT, or LEFT and DOWN
                    this.direction = Direction.VALUES[(ord / 2) * 2 + (1 - ord % 2)];
                    break;
                case '+':
                    counter = (counter + 1) % 3;
                    switch (counter) {
                        case 1:
                            this.direction = Direction.VALUES[(ord + 3) % 4];
                            break;
                        case 0:
                            this.direction = Direction.VALUES[(ord + 1) % 4];
                            break;
                    }
                    break;
            }

            return this;
        }

        public boolean crash(Cart cart) {
            return cart.x == x && cart.y == y;
        }
    }

    public enum Direction {
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0);

        private static Direction[] VALUES = Direction.values();

        private int x;
        private int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public int[] simulate() {
        this.carts = carts.stream()
                .sorted((c1, c2) -> {
                    if (c1.y == c2.y) {
                        return c1.x - c2.x;
                    }
                    return c1.y - c2.y;
                })
                .collect(Collectors.toList());

        for (Cart cart : carts) {
            cart.move(track);

            int[] crash = carts.stream()
                    .filter(c -> !c.equals(cart))
                    .filter(cart::crash)
                    .findFirst()
                    .map(c -> new int[]{c.x, c.y})
                    .orElse(null);

            if (crash != null) {
                return crash;
            }
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day13Part1 day13Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int[] crash = null;
        while (crash == null) {
            crash = day13Part1.simulate();
        }
        System.out.println(crash[0] + "," + crash[1]);
    }

    private static Day13Part1 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<char[]> track = new ArrayList<>();
            List<Cart> carts = new ArrayList<>();

            int y = 0;
            while (scanner.hasNext()) {
                char[] row = scanner.nextLine().toCharArray();
                for (int x = 0; x < row.length; x++) {
                    Direction direction;
                    switch (row[x]) {
                        case '^':
                            row[x] = '|';
                            direction = Direction.UP;
                            break;
                        case 'v':
                            row[x] = '|';
                            direction = Direction.DOWN;
                            break;
                        case '<':
                            row[x] = '-';
                            direction = Direction.LEFT;
                            break;
                        case '>':
                            row[x] = '-';
                            direction = Direction.RIGHT;
                            break;
                        default:
                            continue;
                    }
                    carts.add(new Cart(x, y, direction));
                }

                track.add(row);
                y += 1;
            }
            return new Day13Part1(track.toArray(new char[0][]), carts);
        }
    }
}
