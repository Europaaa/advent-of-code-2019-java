package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day23Part1 {

    private static final String INPUT_FILE = "y2020/day23";

    private Cup pointer;
    private int size;

    public Day23Part1(Cup pointer, int size) {
        this.pointer = pointer;
        this.size = size;
    }

    public void simulate() {
        // Pick up the three cups that are immediately clockwise of the current cup
        Cup c1 = pointer.next;
        Cup c2 = c1.next;
        Cup c3 = c2.next;

        // Remove the three cups from the circle
        pointer.next = c3.next;
        c3.next = null;

        // Select the destination cup id
        int target = pointer.id - 1 == 0 ? size : pointer.id - 1;
        while (target == c1.id || target == c2.id || target == c3.id) {
            target = target - 1 == 0 ? size : target - 1;
        }

        // Find the destination cup
        Cup cup = pointer.next;
        while (cup.id != target) {
            cup = cup.next;
        }

        // Place the three cups after the destination cup
        Cup next = cup.next;

        cup.next = c1;
        c1.next = c2;
        c2.next = c3;
        c3.next = next;

        pointer = pointer.next;
    }

    private static class Cup {
        private int id;
        private Cup next;

        public Cup(int id) {
            this.id = id;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day23Part1 day23Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        for (int i = 0; i < 100; i++) {
            day23Part1.simulate();
        }

        // Find Cup #1
        Cup cup = day23Part1.pointer;
        while (cup.id != 1) {
            cup = cup.next;
        }

        // Find the labels
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < day23Part1.size - 1; i++) {
            cup = cup.next;
            builder.append(cup.id);
        }
        System.out.println(builder.toString());
    }

    private static Day23Part1 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            char[] chars = scanner.nextLine().toCharArray();

            Cup head = new Cup(chars[0] - '0');

            Cup pointer = head;
            for (int i = 1; i < chars.length; i++) {
                pointer.next = new Cup(chars[i] - '0');
                pointer = pointer.next;
            }
            pointer.next = head;

            return new Day23Part1(head, chars.length);
        }
    }
}
