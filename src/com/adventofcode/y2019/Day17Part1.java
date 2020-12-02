package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day17Part1 {

    private static final String INPUT_FILE = "y2019/day17";

    private static final char CELL_OPEN_SPACE = '.';
    private static final char CELL_ROBOT_OFF_SCAFFOLD = 'X';

    public static int getSumOfAlignmentParameters(long[] codes) {
        VacuumRobot robot = new VacuumRobot(codes);
        char[][] cameraView = robot.getCameraView();

        int sum = 0;
        for (int y = 0; y < cameraView.length; y++) {
            for (int x = 0; x < cameraView[0].length; x++) {
                if (isScaffold(cameraView, x, y)
                        && isScaffold(cameraView, x, y + 1)
                        && isScaffold(cameraView, x + 1, y)
                        && isScaffold(cameraView, x, y - 1)
                        && isScaffold(cameraView, x - 1, y)) {
                    sum += x * y;
                }
            }
        }
        return sum;
    }

    private static boolean isScaffold(char[][] cameraView, int x, int y) {
        return x >= 0 && x < cameraView[0].length && y >= 0 && y < cameraView.length && cameraView[y][x] != CELL_OPEN_SPACE && cameraView[y][x] != CELL_ROBOT_OFF_SCAFFOLD;
    }

    static class VacuumRobot {

        private IntcodeComputer computer;

        VacuumRobot(long[] codes) {
            this.computer = new IntcodeComputer(codes);
        }

        public char[][] getCameraView() {
            this.computer.evaluate();

            List<char[]> view = new ArrayList<>();

            StringBuilder builder = new StringBuilder();
            while (!this.computer.outputs.isEmpty()) {
                char ascii = (char) this.computer.outputs.poll().intValue();
                switch (ascii) {
                    case '\n':
                        char[] chars = builder.toString().toCharArray();
                        if (chars.length > 0) {
                            view.add(chars);
                        }
                        builder = new StringBuilder();
                        break;
                    default:
                        builder.append(ascii);
                        break;
                }
            }
            return view.toArray(new char[0][]);
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
        System.out.println(getSumOfAlignmentParameters(codes));
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
