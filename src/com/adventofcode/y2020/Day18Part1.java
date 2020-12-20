package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day18Part1 {

    private static final String INPUT_FILE = "y2020/day18";

    static class Response {
        private long result;
        private int pointer;

        public Response setResult(long result) {
            this.result = result;
            return this;
        }

        public Response setPointer(int pointer) {
            this.pointer = pointer;
            return this;
        }
    }

    public static long evaluate(String expression) {
        Response response = evaluate(expression.toCharArray(), 0, 0, '+');
        return response.result;
    }

    public static Response evaluate(char[] expression, int start, long operand, char operator) {
        Response response = getOperand(expression, start);

        long value = response.result;
        switch (operator) {
            case '+':
                value += operand;
                break;
            case '*':
                value *= operand;
                break;
        }

        if (response.pointer + 1 >= expression.length || expression[response.pointer] == ')') {
            return new Response()
                    .setResult(value)
                    .setPointer(response.pointer);
        }
        return evaluate(expression, response.pointer + 3, value, expression[response.pointer + 1]);
    }

    private static Response getOperand(char[] expression, int start) {
        if (expression[start] == '(') {
            Response res = evaluate(expression, start + 1, 0, '+');
            return new Response()
                    .setResult(res.result)
                    // Advance the pointer to skip the right bracket
                    .setPointer(res.pointer + 1);
        }

        StringBuilder builder = new StringBuilder();
        int i = start;
        for (; i < expression.length && '0' <= expression[i] && expression[i] <= '9'; i++) {
            builder.append(expression[i]);
        }
        return new Response()
                .setResult(Long.parseLong(builder.toString()))
                .setPointer(i);
    }


    public static void main(String[] args) throws FileNotFoundException {
        List<String> expressions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long sum = expressions.stream().mapToLong(Day18Part1::evaluate).sum();
        System.out.println(sum);
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> expressions = new ArrayList<>();
            while (scanner.hasNext()) {
                expressions.add(scanner.nextLine());
            }
            return expressions;
        }
    }
}
