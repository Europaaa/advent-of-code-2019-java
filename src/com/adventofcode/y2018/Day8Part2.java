package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day8Part2 {
    private static final String INPUT_FILE = "y2018/day8";

    public static long calculate(Integer[] numbers) {
        Node root = getRootNode(numbers);
        return root.getValue();
    }

    private static Node getRootNode(Integer[] numbers) {
        Stack<Node> stack = new Stack<>();

        List<Integer> metadata = new LinkedList<>();
        for (int i = 0; i < numbers.length; ) {
            if (stack.isEmpty()) {
                stack.add(new Node(numbers[i], numbers[i + 1]));
                i += 2;
                continue;
            }

            Node node = stack.peek();

            if (node.children.size() != node.numChild) {
                Node child = new Node(numbers[i], numbers[i + 1]);
                node.children.add(child);

                stack.add(child);
                i += 2;
                continue;
            }

            if (node.metadata.size() != node.numMetadata) {
                node.metadata.add(numbers[i]);
                i += 1;
                continue;
            }

            metadata.addAll(node.metadata);
            stack.pop();
        }

        return stack.pop();
    }

    private static class Node {
        private int numChild;
        private int numMetadata;

        private List<Node> children;
        private List<Integer> metadata;

        private long value;

        public Node(int numChild, int numMetadata) {
            this.numChild = numChild;
            this.numMetadata = numMetadata;

            this.children = new ArrayList<>();
            this.metadata = new ArrayList<>();

            this.value = -1L;
        }

        private long getValue() {
            if (value >= 0) {
                return value;
            }

            if (numChild == 0) {
                value = metadata.stream().mapToInt(i -> i).sum();
                return value;
            }

            value = metadata.stream()
                    .filter(i -> i > 0 && i <= numChild)
                    .map(i -> children.get(i - 1))
                    .mapToLong(Node::getValue)
                    .sum();
            return value;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Integer[] numbers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long value = calculate(numbers);
        System.out.println(value);
    }

    private static Integer[] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Integer> numbers = new ArrayList<>();
            while (scanner.hasNext()) {
                numbers.add(scanner.nextInt());
            }
            return numbers.toArray(new Integer[0]);
        }
    }
}
