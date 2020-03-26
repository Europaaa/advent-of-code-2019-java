package com.adventofcode.y2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day13Part2 {

    private static final String INPUT_FILE = "y2019/day13";

    public static int getHighScore(long[] codes) {
        IntcodeComputer computer = new IntcodeComputer(codes);
        computer.evaluate();

        ArcadeGame arcadeGame = new ArcadeGame();
        arcadeGame.updateState(computer.outputs);

        while (arcadeGame.board.values().stream().anyMatch(x -> x == ArcadeGame.TileType.BLOCK)) {
            computer.inputs.add((long) arcadeGame.getNextMove());
            computer.evaluate();

            arcadeGame.updateState(computer.outputs);
        }
        return arcadeGame.score;
    }

    static class ArcadeGame {

        private static final int SCORE_X = -1;
        private static final int SCORE_Y = 0;

        private int score;
        private Map<Position, TileType> board;

        ArcadeGame() {
            this.score = 0;
            this.board = new HashMap<>();
        }

        private void updateState(LinkedList<Long> values) {
            while (!values.isEmpty()) {
                int x = values.poll().intValue();
                int y = values.poll().intValue();
                int t = values.poll().intValue();

                if (x == SCORE_X && y == SCORE_Y) {
                    this.score = t;
                } else {
                    this.board.put(new Position(x, y), getTileType(t));
                }
            }
        }

        private int getNextMove() {
            long xPaddle = this.board.entrySet().stream()
                    .filter(entry -> entry.getValue() == TileType.HORIZONTAL_PADDLE)
                    .findFirst()
                    .map(entry -> entry.getKey().x)
                    .get();

            long xBall = this.board.entrySet().stream()
                    .filter(entry -> entry.getValue() == TileType.BALL)
                    .findFirst()
                    .map(entry -> entry.getKey().x)
                    .get();

            if (xPaddle == xBall) {
                return 0;
            }
            return xPaddle < xBall ? 1 : -1;
        }

        private static TileType getTileType(int signal) {
            switch (signal) {
                case 1:
                    return TileType.WALL;
                case 2:
                    return TileType.BLOCK;
                case 3:
                    return TileType.HORIZONTAL_PADDLE;
                case 4:
                    return TileType.BALL;
                default:
                    return TileType.EMPTY;
            }
        }

        enum TileType {
            EMPTY, WALL, BLOCK, HORIZONTAL_PADDLE, BALL
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

    public static void main(String[] args) throws FileNotFoundException {
        long[] codes = getInput(CommonUtils.getInputFile(INPUT_FILE));
        System.out.println(getHighScore(codes));
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
