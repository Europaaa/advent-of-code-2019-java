package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2Part2 {
    private static final String INPUT_FILE = "y2018/day2";

    public static String calculate(List<char[]> ids) {
        for (char[] id1 : ids) {
            for (char[] id2 : ids) {
                if (id1.length != id2.length) {
                    continue;
                }

                long diff = 0;
                for (int i = 0; i < id1.length; i++) {
                    if (id1[i] == id2[i]) {
                        continue;
                    }
                    diff += 1;
                    if (diff > 1) {
                        break;
                    }
                }

                if (diff != 1) {
                    continue;
                }

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < id1.length; i++) {
                    if (id1[i] == id2[i]) {
                        builder.append(id1[i]);
                    }
                }
                return builder.toString();
            }
        }
        throw new IllegalStateException();
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<char[]> ids = getInput(CommonUtils.getInputFile(INPUT_FILE));

        String commonLetters = calculate(ids);
        System.out.println(commonLetters);
    }

    private static List<char[]> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<char[]> ids = new ArrayList<>();
            while (scanner.hasNext()) {
                ids.add(scanner.nextLine().toCharArray());
            }
            return ids;
        }
    }
}
