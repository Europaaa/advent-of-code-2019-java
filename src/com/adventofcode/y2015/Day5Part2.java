package com.adventofcode.y2015;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day5Part2 {

    private static final String INPUT_FILE = "y2015/day5";

    private static long evaluate(List<String> codes) {
        return codes.stream().filter(Day5Part2::isNice).count();
    }

    private static boolean isNice(String code) {
        return code.matches(".*(.).\\1.*") && code.matches(".*(..).*\\1.*");
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> codes = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(codes));
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> inputs = new ArrayList<>();
            while (scanner.hasNext()) {
                inputs.add(scanner.nextLine());
            }
            return inputs;
        }
    }
}
