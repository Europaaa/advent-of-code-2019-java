package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day7Part2 {

    private static final String INPUT_FILE = "y2019/day7";

    public static int getMaxSignal(int[] codes) {
        Set<Integer> numbers = new HashSet<>(Arrays.asList(5, 6, 7, 8, 9));
        List<int[]> permutations = getPermutations(numbers, new int[0]);

        int signal = getSignal(codes, permutations.get(0));
        for (int[] phases : permutations) {
            signal = Math.max(signal, getSignal(codes, phases));
        }
        return signal;
    }

    private static List<int[]> getPermutations(Set<Integer> candidates, int[] current) {
        if (candidates.isEmpty()) {
            return Arrays.asList(current);
        }

        List<int[]> permutations = new ArrayList<>();
        for (Integer candidate : candidates) {
            HashSet<Integer> newCandidates = new HashSet<>(candidates);
            newCandidates.remove(candidate);

            int[] permutation = Arrays.copyOf(current, current.length + 1);
            permutation[current.length] = candidate;

            permutations.addAll(getPermutations(newCandidates, permutation));
        }
        return permutations;
    }

    private static int getSignal(int[] codes, int[] phases) {
        IntcodeComputer[] computers = new IntcodeComputer[phases.length];

        for (int i = 0; i < phases.length; i++) {
            computers[i] = new IntcodeComputer(codes);
            computers[i].inputs.add(phases[i]);
        }
        computers[0].inputs.add(0);

        int lastOutput = 0;
        for (int i = 0; i < phases.length && computers[i].state == IntcodeComputer.State.WAITING_FOR_INPUT; i = (i + 1) % phases.length) {
            computers[i].evaluate();
            computers[(i + 1) % phases.length].inputs.addAll(computers[i].outputs);
            if (i == phases.length - 1) {
                lastOutput = computers[i].outputs.peekLast();
            }
            computers[i].outputs.clear();
        }
        return lastOutput;
    }

    static class IntcodeComputer {

        private LinkedList<Integer> inputs;
        private LinkedList<Integer> outputs;
        private State state;

        private int[] codes;
        private int pointer;

        enum State {
            RUNNING, WAITING_FOR_INPUT, HALTED, EXITED, ERROR
        }

        IntcodeComputer(int[] codes) {
            this.inputs = new LinkedList<>();
            this.outputs = new LinkedList<>();
            this.state = State.WAITING_FOR_INPUT;

            this.codes = codes;
            this.pointer = 0;
        }

        public void evaluate() {
            this.state = State.RUNNING;

            while (this.pointer < this.codes.length) {
                Integer optcode = this.codes[this.pointer];

                switch (optcode % 100) {
                    case 1:
                        char[] modes = String.format("%05d", optcode).toCharArray();
                        int op1 = modes[2] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        int op2 = modes[1] == '1' ? this.pointer + 2 : this.codes[this.pointer + 2];
                        int op3 = modes[0] == '1' ? this.pointer + 3 : this.codes[this.pointer + 3];
                        this.codes[op3] = this.codes[op1] + this.codes[op2];
                        this.pointer = this.pointer + 4;
                        break;
                    case 2:
                        modes = String.format("%05d", optcode).toCharArray();
                        op1 = modes[2] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        op2 = modes[1] == '1' ? this.pointer + 2 : this.codes[this.pointer + 2];
                        op3 = modes[0] == '1' ? this.pointer + 3 : this.codes[this.pointer + 3];
                        this.codes[op3] = this.codes[op1] * this.codes[op2];
                        this.pointer = this.pointer + 4;
                        break;
                    case 3:
                        if (this.inputs.isEmpty()) {
                            this.state = State.WAITING_FOR_INPUT;
                            return;
                        }
                        modes = String.format("%03d", optcode).toCharArray();
                        op1 = modes[0] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        this.codes[op1] = this.inputs.poll();
                        this.pointer = this.pointer + 2;
                        break;
                    case 4:
                        modes = String.format("%03d", optcode).toCharArray();
                        op1 = modes[0] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        this.outputs.add(this.codes[op1]);
                        this.pointer = this.pointer + 2;
                        break;
                    case 5:
                        modes = String.format("%04d", optcode).toCharArray();
                        op1 = modes[1] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        op2 = modes[0] == '1' ? this.pointer + 2 : this.codes[this.pointer + 2];
                        if (this.codes[op1] != 0) {
                            this.pointer = this.codes[op2];
                        } else {
                            this.pointer = this.pointer + 3;
                        }
                        break;
                    case 6:
                        modes = String.format("%04d", optcode).toCharArray();
                        op1 = modes[1] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        op2 = modes[0] == '1' ? this.pointer + 2 : this.codes[this.pointer + 2];
                        if (this.codes[op1] == 0) {
                            this.pointer = this.codes[op2];
                        } else {
                            this.pointer = this.pointer + 3;
                        }
                        break;
                    case 7:
                        modes = String.format("%05d", optcode).toCharArray();
                        op1 = modes[2] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        op2 = modes[1] == '1' ? this.pointer + 2 : this.codes[this.pointer + 2];
                        op3 = modes[0] == '1' ? this.pointer + 3 : this.codes[this.pointer + 3];
                        this.codes[op3] = this.codes[op1] < this.codes[op2] ? 1 : 0;
                        this.pointer = this.pointer + 4;
                        break;
                    case 8:
                        modes = String.format("%05d", optcode).toCharArray();
                        op1 = modes[2] == '1' ? this.pointer + 1 : this.codes[this.pointer + 1];
                        op2 = modes[1] == '1' ? this.pointer + 2 : this.codes[this.pointer + 2];
                        op3 = modes[0] == '1' ? this.pointer + 3 : this.codes[this.pointer + 3];
                        this.codes[op3] = this.codes[op1] == this.codes[op2] ? 1 : 0;
                        this.pointer = this.pointer + 4;
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
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] codes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int maxSignal = getMaxSignal(codes);
        System.out.println(maxSignal);
    }

    private static int[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.useDelimiter(",");

            List<Integer> codes = new ArrayList<>();
            while (scanner.hasNext()) {
                codes.add(scanner.nextInt());
            }

            return codes.stream().mapToInt(i -> i).toArray();
        }
    }
}
