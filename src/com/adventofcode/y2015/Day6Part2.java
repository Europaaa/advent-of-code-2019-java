package com.adventofcode.y2015;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6Part2 {

    private static final String INPUT_FILE = "y2015/day6";

    private static final int DIMENSION = 999;

    private static long evaluate(List<Command> commands) {
        int[][] lights = new int[DIMENSION + 1][DIMENSION + 1];

        commands.stream().forEachOrdered(command -> {
            int x1 = command.coord1.x;
            int x2 = command.coord2.x;
            int y1 = command.coord1.y;
            int y2 = command.coord2.y;

            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    switch (command.type) {
                        case ON:
                            lights[x][y] += 1;
                            break;
                        case OFF:
                            lights[x][y] = Math.max(0, lights[x][y] - 1);
                            break;
                        case TOGGLE:
                            lights[x][y] += 2;
                            break;
                    }
                }
            }
        });

        long brightness = 0;
        for (int i = 0; i < lights.length; i++) {
            brightness += Arrays.stream(lights[i]).sum();
        }
        return brightness;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Command> commands = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(commands));
    }

    private static List<Command> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Command> inputs = new ArrayList<>();

            while (scanner.hasNext()) {
                String code = scanner.nextLine();

                Command command = Arrays.stream(CommandType.values())
                        .map(op -> {
                            Matcher matcher = op.regex.matcher(code);
                            if (matcher.matches()) {
                                int x1 = Integer.valueOf(matcher.group(1));
                                int y1 = Integer.valueOf(matcher.group(2));
                                int x2 = Integer.valueOf(matcher.group(3));
                                int y2 = Integer.valueOf(matcher.group(4));

                                Coordinate coord1 = new Coordinate(x1, y1);
                                Coordinate coord2 = new Coordinate(x2, y2);
                                return new Command(op, coord1, coord2);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .findFirst()
                        .get();

                inputs.add(command);
            }
            return inputs;
        }
    }

    private static class Coordinate {
        private int x;
        private int y;

        private Coordinate(int x, int y) {
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

    private enum CommandType {
        ON("turn on (\\d+),(\\d+) through (\\d+),(\\d+)"),
        OFF("turn off (\\d+),(\\d+) through (\\d+),(\\d+)"),
        TOGGLE("toggle (\\d+),(\\d+) through (\\d+),(\\d+)");

        private Pattern regex;

        CommandType(String regex) {
            this.regex = Pattern.compile(regex);
        }
    }

    private static class Command {
        private CommandType type;
        private Coordinate coord1;
        private Coordinate coord2;

        public Command(CommandType type, Coordinate coord1, Coordinate coord2) {
            this.type = type;
            this.coord1 = coord1;
            this.coord2 = coord2;
        }
    }
}
