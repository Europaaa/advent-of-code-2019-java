package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day8Part1 {

    private static final String INPUT_FILE = "y2015/day8";

    public static long evaluate(List<String> codes) {
        return codes.stream()
                .map(c -> c.substring(1, c.length() - 1))
                .mapToLong(Day8Part1::evaluate).sum() + codes.size() * 2;
    }

    private static long evaluate(String code) {
        char[] chars = code.toCharArray();

        long size = 0;
        for (int i = 0; i < chars.length; ) {
            if (chars[i] != '\\') {
                i += 1;
                continue;
            }

            int diff = 0;
            if (i + 1 < chars.length && (chars[i + 1] == '\\' || chars[i + 1] == '"')) {
                diff += 1;
            }
            if (i + 3 < chars.length && chars[i + 1] == 'x' && isHex(chars[i + 2]) && isHex(chars[i + 3])) {
                diff += 3;
            }
            size += diff;
            i += diff + 1;
        }
        return size;
    }

    private static boolean isHex(char c) {
        return ('0' <= c && c <= '9') || ('a' <= c && c <= 'z');
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> codes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(codes));
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> codes = new ArrayList<>();
            while (scanner.hasNext()) {
                codes.add(scanner.nextLine());
            }
            return codes;
        }
    }
}
