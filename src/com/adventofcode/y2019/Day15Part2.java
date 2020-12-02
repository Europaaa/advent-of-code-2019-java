package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day15Part2 {

    private static final String INPUT_FILE = "y2019/day15";

    private static final char CELL_WALL = '#';
    private static final char CELL_EMPTY = '.';
    private static final char CELL_TARGET = 'D';
    private static final char CELL_OXYGEN = 'O';

    private static final int[] DIRECTIONS = new int[]{1, 2, 3, 4};

    public static int getMinSteps(long[] codes) {
        RepairDroid droid = new RepairDroid(codes);
        explore(droid);

        Position position = droid.map.entrySet().stream()
                .filter(entry -> entry.getValue() == CELL_TARGET)
                .findFirst().get().getKey();

        Set<Position> targets = droid.map.entrySet().stream()
                .filter(entry -> entry.getValue() == CELL_EMPTY)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return spread(Arrays.asList(position), targets);
    }

    private static int spread(List<Position> positions, Set<Position> targets) {
        if (targets.isEmpty()) {
            return 0;
        }

        List<Position> newPositions = new ArrayList<>();
        for (Position position : positions) {
            Position p1 = new Position(position.x + 1, position.y);
            Position p2 = new Position(position.x - 1, position.y);
            Position p3 = new Position(position.x, position.y + 1);
            Position p4 = new Position(position.x, position.y - 1);

            if (targets.remove(p1)) {
                newPositions.add(p1);
            }
            if (targets.remove(p2)) {
                newPositions.add(p2);
            }
            if (targets.remove(p3)) {
                newPositions.add(p3);
            }
            if (targets.remove(p4)) {
                newPositions.add(p4);
            }
        }
        return 1 + spread(newPositions, targets);
    }

    private static void explore(RepairDroid droid) {
        // Explore different directions
        for (int i = 0; i < DIRECTIONS.length; i++) {
            Position position = droid.getPosition(DIRECTIONS[i]);

            // Do not repeat yourself..
            if (droid.map.containsKey(position)) {
                continue;
            }

            droid.explore(DIRECTIONS[i]);

            switch (droid.map.get(position)) {
                case CELL_TARGET:
                    droid.explore(getOppositeDirection(DIRECTIONS[i]));
                    break;
                case CELL_EMPTY:
                    explore(droid);
                    droid.explore(getOppositeDirection(DIRECTIONS[i]));
                    break;
                case CELL_WALL:
                    // The droid didn't move in this case. Continue exploring other directions
                    break;
            }
        }
    }

    private static int getOppositeDirection(int direction) {
        switch (direction) {
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 4;
            case 4:
                return 3;
        }
        throw new IllegalArgumentException();
    }

    static class RepairDroid {

        private Position pointer;
        private Map<Position, Character> map;

        private IntcodeComputer computer;

        RepairDroid(long[] codes) {
            this.pointer = new Position(0, 0);

            this.map = new HashMap<>();
            this.map.put(this.pointer, CELL_EMPTY);

            this.computer = new IntcodeComputer(codes);
        }

        public boolean explore(int direction) {
            Position position = getPosition(direction);
            computer.inputs.add((long) direction);
            computer.evaluate();

            int status = computer.outputs.poll().intValue();
            return updateState(position, status);
        }

        private Position getPosition(int direction) {
            switch (direction) {
                case 1:
                    return getPosition(0, -1);
                case 2:
                    return getPosition(0, 1);
                case 3:
                    return getPosition(-1, 0);
                case 4:
                    return getPosition(1, 0);
            }
            throw new IllegalArgumentException();
        }

        private Position getPosition(int directionX, int directionY) {
            long x = pointer.x + directionX;
            long y = pointer.y + directionY;
            return new Position(x, y);
        }

        private boolean updateState(Position position, int signal) {
            switch (signal) {
                case 0:
                    this.map.put(position, CELL_WALL);
                    return false;
                case 1:
                    this.pointer = position;
                    this.map.put(this.pointer, CELL_EMPTY);
                    return false;
                default:
                    this.pointer = position;
                    this.map.put(this.pointer, CELL_TARGET);
                    return true;
            }
        }
    }

    static class Position {
        long x;
        long y;

        Position(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            Position other = (Position) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    static class IntcodeComputer {

        public LinkedList<Long> inputs;
        public LinkedList<Long> outputs;
        public State state;

        public Map<Long, Long> codes;
        public long pointer;
        public long relativeBase;

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

        public IntcodeComputer clone() {
            IntcodeComputer computer = new IntcodeComputer(new long[0]);
            computer.inputs = this.inputs;
            computer.outputs = this.outputs;
            computer.state = this.state;
            computer.codes = this.codes;
            computer.pointer = this.pointer;
            computer.relativeBase = this.relativeBase;
            return computer;
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

    public static void main(String[] args) throws FileNotFoundException {
        long[] codes = getInput(CommonUtils.getInputFile(INPUT_FILE));
        System.out.println(getMinSteps(codes));
    }

    private static long[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.useDelimiter(",");
            scanner.useDelimiter(",");

            List<Long> codes = new ArrayList<>();
            while (scanner.hasNext()) {
                codes.add(scanner.nextLong());
            }
            return codes.stream().mapToLong(i -> i).toArray();
        }
    }
}
