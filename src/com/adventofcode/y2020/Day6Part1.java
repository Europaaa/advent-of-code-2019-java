package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day6Part1 {

    private static final String INPUT_FILE = "y2020/day6";

    public static int countAnswers(String[] answers) {
        boolean[] commonAnswers = new boolean[26];
        Arrays.fill(commonAnswers, false);

        for (int i = 0; i < answers.length; i++) {
            char[] chars = answers[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                commonAnswers[chars[j] - 'a'] = true;
            }
        }

        int count = 0;
        for (int i = 0; i < commonAnswers.length; i++) {
            if (commonAnswers[i]) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<String>> answers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Long result = answers.stream()
                .map(ans -> ans.toArray(new String[0]))
                .mapToLong(Day6Part1::countAnswers).sum();
        System.out.println(result);
    }

    private static List<List<String>> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<List<String>> answers = new ArrayList<>();
            while (scanner.hasNext()) {
                List<String> group = new ArrayList<>();

                String line = scanner.nextLine().trim();
                while (!line.isEmpty() && scanner.hasNext()) {
                    group.add(line);
                    line = scanner.nextLine().trim();
                }

                if (!line.isEmpty()) {
                    group.add(line);
                }

                answers.add(group);
            }
            return answers;
        }
    }
}
