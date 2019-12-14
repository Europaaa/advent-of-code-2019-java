package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day11Part2 {

    private static final String INPUT_FILE = "day11";

    private static void printRegistrationIdentifier(long[] codes) {
        Robot robot = new Robot();
        evaluate(robot, new IntcodeComputer(codes));

        // Find the size of the map and calculate offsets
        List<int[]> coords = robot.colors.keySet().stream().map(Robot::decodeKey).collect(Collectors.toList());

        int minX = coords.stream().mapToInt(c -> c[0]).min().getAsInt();
        int maxX = coords.stream().mapToInt(c -> c[0]).max().getAsInt();
        int minY = coords.stream().mapToInt(c -> c[1]).min().getAsInt();
        int maxY = coords.stream().mapToInt(c -> c[1]).max().getAsInt();

        int width = maxX - minX + 1;
        int height = maxY - minY + 1;

        int offsetX = -minX;
        int offsetY = -minY;

        char[][] canvas = new char[height][width];
        Arrays.stream(canvas).forEach(c -> Arrays.fill(c, ' '));

        robot.colors.entrySet().stream()
                .forEach(entry -> {
                    int[] c = Robot.decodeKey(entry.getKey());
                    int x = c[0] + offsetX;
                    int y = c[1] + offsetY;
                    canvas[y][x] = entry.getValue() == Robot.COLOR_BLACK ? ' ' : '#';
                });

        Arrays.stream(canvas).forEach(System.out::println);
    }

    public static void evaluate(Robot robot, IntcodeComputer computer) {
        while (computer.state == IntcodeComputer.State.WAITING_FOR_INPUT) {
            computer.inputs.add(Long.valueOf(robot.getColor()));
            computer.evaluate();

            Long color = computer.outputs.poll();
            robot.paint(color.intValue());

            Long direction = computer.outputs.poll();
            if (direction == 0) {
                robot.turnLeft();
            } else {
                robot.turnRight();
            }
        }
    }

    static class IntcodeComputer {

        private LinkedList<Long> inputs;
        private LinkedList<Long> outputs;
        private State state;

        private Map<Long, Long> codes;
        private long pointer;
        private long relativeBase;

        enum State {
            RUNNING, WAITING_FOR_INPUT, HALTED, EXITED, ERROR
        }

        IntcodeComputer(long[] codes) {
            this.inputs = new LinkedList<>();
            this.outputs = new LinkedList<>();
            this.state = State.WAITING_FOR_INPUT;

            this.codes = LongStream.range(0, codes.length).boxed().collect(Collectors.toMap(Function.identity(), i -> codes[i.intValue()]));
            this.pointer = 0;
            this.relativeBase = 0;
        }

        public void evaluate() {
            this.state = State.RUNNING;

            while (this.codes.containsKey(this.pointer)) {
                int optcode = this.codes.getOrDefault(this.pointer, 0l).intValue();

                switch (optcode % 100) {
                    case 1:
                        char[] modes = String.format("%05d", optcode).toCharArray();
                        long op1 = getAddress(modes[2], this.pointer + 1);
                        long op2 = getAddress(modes[1], this.pointer + 2);
                        long op3 = getAddress(modes[0], this.pointer + 3);
                        this.codes.put(op3, this.codes.getOrDefault(op1, 0l) + this.codes.getOrDefault(op2, 0l));
                        this.pointer = this.pointer + 4;
                        break;
                    case 2:
                        modes = String.format("%05d", optcode).toCharArray();
                        op1 = getAddress(modes[2], this.pointer + 1);
                        op2 = getAddress(modes[1], this.pointer + 2);
                        op3 = getAddress(modes[0], this.pointer + 3);
                        this.codes.put(op3, this.codes.getOrDefault(op1, 0l) * this.codes.getOrDefault(op2, 0l));
                        this.pointer = this.pointer + 4;
                        break;
                    case 3:
                        if (this.inputs.isEmpty()) {
                            this.state = State.WAITING_FOR_INPUT;
                            return;
                        }
                        modes = String.format("%03d", optcode).toCharArray();
                        op1 = getAddress(modes[0], this.pointer + 1);
                        this.codes.put(op1, this.inputs.poll());
                        this.pointer = this.pointer + 2;
                        break;
                    case 4:
                        modes = String.format("%03d", optcode).toCharArray();
                        op1 = getAddress(modes[0], this.pointer + 1);
                        this.outputs.add(this.codes.getOrDefault(op1, 0l));
                        this.pointer = this.pointer + 2;
                        break;
                    case 5:
                        modes = String.format("%04d", optcode).toCharArray();
                        op1 = getAddress(modes[1], this.pointer + 1);
                        op2 = getAddress(modes[0], this.pointer + 2);
                        if (this.codes.getOrDefault(op1, 0l) != 0l) {
                            this.pointer = this.codes.getOrDefault(op2, 0l);
                        } else {
                            this.pointer = this.pointer + 3;
                        }
                        break;
                    case 6:
                        modes = String.format("%04d", optcode).toCharArray();
                        op1 = getAddress(modes[1], this.pointer + 1);
                        op2 = getAddress(modes[0], this.pointer + 2);
                        if (this.codes.getOrDefault(op1, 0l) == 0l) {
                            this.pointer = this.codes.getOrDefault(op2, 0l);
                        } else {
                            this.pointer = this.pointer + 3;
                        }
                        break;
                    case 7:
                        modes = String.format("%05d", optcode).toCharArray();
                        op1 = getAddress(modes[2], this.pointer + 1);
                        op2 = getAddress(modes[1], this.pointer + 2);
                        op3 = getAddress(modes[0], this.pointer + 3);
                        this.codes.put(op3, this.codes.getOrDefault(op1, 0l) < this.codes.getOrDefault(op2, 0l) ? 1l : 0l);
                        this.pointer = this.pointer + 4;
                        break;
                    case 8:
                        modes = String.format("%05d", optcode).toCharArray();
                        op1 = getAddress(modes[2], this.pointer + 1);
                        op2 = getAddress(modes[1], this.pointer + 2);
                        op3 = getAddress(modes[0], this.pointer + 3);
                        this.codes.put(op3, this.codes.getOrDefault(op1, 0l).longValue() == this.codes.getOrDefault(op2, 0l).longValue() ? 1l : 0l);
                        this.pointer = this.pointer + 4;
                        break;
                    case 9:
                        modes = String.format("%03d", optcode).toCharArray();
                        op1 = getAddress(modes[0], this.pointer + 1);
                        this.relativeBase += this.codes.getOrDefault(op1, 0l);
                        this.pointer = this.pointer + 2;
                        break;
                    case 99:
                        this.state = State.HALTED;
                        this.pointer = this.pointer + 1;
                        return;
                    default:
                        this.state = State.ERROR;
                        throw new IllegalArgumentException("Invalid input for the program");
                }
            }
            this.state = State.EXITED;
            return;
        }

        private long getAddress(char mode, long pointer) {
            switch (mode) {
                case '1':
                    return pointer;
                case '2':
                    return this.relativeBase + this.codes.getOrDefault(pointer, 0l);
                default:
                    return this.codes.getOrDefault(pointer, 0l);
            }
        }
    }

    static class Robot {
        private int x;
        private int y;

        private int directionX;
        private int directionY;

        private Map<String, Integer> colors;

        private static final int COLOR_BLACK = 0;
        private static final int COLOR_WHITE = 1;

        Robot() {
            this.x = 0;
            this.y = 0;

            this.directionX = 0;
            this.directionY = -1;

            this.colors = new HashMap<>();
        }

        public void turnLeft() {
            if (this.directionX == 0 && this.directionY == -1) {
                this.directionX = -1;
                this.directionY = 0;
            } else if (this.directionX == -1 && this.directionY == 0) {
                this.directionX = 0;
                this.directionY = 1;
            } else if (this.directionX == 0 && this.directionY == 1) {
                this.directionX = 1;
                this.directionY = 0;
            } else {
                this.directionX = 0;
                this.directionY = -1;
            }

            this.x = this.x + this.directionX;
            this.y = this.y + this.directionY;
        }

        public void turnRight() {
            if (this.directionX == 0 && this.directionY == -1) {
                this.directionX = 1;
                this.directionY = 0;
            } else if (this.directionX == 1 && this.directionY == 0) {
                this.directionX = 0;
                this.directionY = 1;
            } else if (this.directionX == 0 && this.directionY == 1) {
                this.directionX = -1;
                this.directionY = 0;
            } else {
                this.directionX = 0;
                this.directionY = -1;
            }

            this.x = this.x + this.directionX;
            this.y = this.y + this.directionY;
        }

        public void paint(int color) {
            this.colors.put(encodeKey(), color);
        }

        public Integer getColor() {
            int defaultColor = this.x == 0 && this.y == 0 ? COLOR_WHITE : COLOR_BLACK;
            return this.colors.getOrDefault(encodeKey(), defaultColor);
        }

        private String encodeKey() {
            return this.x + ":" + this.y;
        }

        private static int[] decodeKey(String key) {
            String[] tokens = key.split(":");
            return new int[]{Integer.valueOf(tokens[0]), Integer.valueOf(tokens[1])};
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        long[] codes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        printRegistrationIdentifier(codes);
    }

    private static long[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.useDelimiter(",");

            List<Long> codes = new ArrayList<>();
            while (scanner.hasNext()) {
                codes.add(scanner.nextLong());
            }

            return codes.stream().mapToLong(i -> i).toArray();
        }
    }
}
