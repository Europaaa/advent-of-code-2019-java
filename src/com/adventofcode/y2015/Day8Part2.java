package com.adventofcode.y2015;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day8Part2 {

    private static final String INPUT_FILE = "y2015/day8";

    public static long evaluate(List<String> codes) {
        return codes.stream().mapToLong(Day8Part2::evaluate).sum() + codes.size() * 2;
    }

    private static long evaluate(String code) {
        char[] chars = code.toCharArray();

        long size = 0;
        for (char c : chars) {
            switch (c) {
                case '"':
                    size += 1;
                    break;
                case '\\':
                    size += 1;
            }
        }
        return size;
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
