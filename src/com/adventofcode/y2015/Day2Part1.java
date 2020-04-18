package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2Part1 {

    private static final String INPUT_FILE = "y2015/day2";

    private static final String SEPARATOR = "\\n|x";

    public static long evaluate(List<int[]> dimensions) {
        return dimensions.stream().mapToLong(Day2Part1::evaluate).sum();
    }

    private static long evaluate(int[] dimensions) {
        long area1 = dimensions[0] * dimensions[1];
        long area2 = dimensions[0] * dimensions[2];
        long area3 = dimensions[1] * dimensions[2];
        return 2 * (area1 + area2 + area3) + Math.min(area1, Math.min(area2, area3));
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<int[]> dimensions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(dimensions));
    }

    private static List<int[]> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.useDelimiter(SEPARATOR);

            List<int[]> dimensions = new ArrayList<>();
            while (scanner.hasNext()) {
                dimensions.add(new int[]{scanner.nextInt(), scanner.nextInt(), scanner.nextInt()});
            }
            return dimensions;
        }
    }
}
