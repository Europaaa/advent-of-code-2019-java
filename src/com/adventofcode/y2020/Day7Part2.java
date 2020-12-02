package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7Part2 {

    private static final String INPUT_FILE = "y2020/day7";

    private static final String PATTERN_STR_SUBBAG = "(?:(\\d+) ([^,]+)) bag(?:s)?";
    private static final String PATTERN_STR_BAG = "(.*) bags contain (?:(no other bags)|((?:" + PATTERN_STR_SUBBAG + ", )*" + PATTERN_STR_SUBBAG + "))\\.";

    private static final Pattern PATTERN_SUBBAG = Pattern.compile(PATTERN_STR_SUBBAG);
    private static final Pattern PATTERN_BAG = Pattern.compile(PATTERN_STR_BAG);

    public static long countBags(Map<String, Map<String, Integer>> bags, String target) {
        Map<String, Integer> subBags = bags.get(target);
        if (subBags.isEmpty()) {
            return 0L;
        }

        long count = 0L;
        for (String subBag : subBags.keySet()) {
            count += (countBags(bags, subBag) + 1) * subBags.get(subBag);
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Map<String, Integer>> bags = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = countBags(bags, "shiny gold");
        System.out.println(result);
    }

    private static Map<String, Map<String, Integer>> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Map<String, Map<String, Integer>> bags = new HashMap<>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                Matcher matcher = PATTERN_BAG.matcher(line);
                if (!matcher.matches()) {
                    continue;
                }

                String bagColor = matcher.group(1);

                // Contains no other bags
                if (matcher.group(2) != null) {
                    bags.put(bagColor, Collections.emptyMap());
                    continue;
                }

                String subBagsText = matcher.group(3);
                matcher = PATTERN_SUBBAG.matcher(subBagsText);

                Map<String, Integer> subBags = new HashMap<>();
                while (matcher.find()) {
                    subBags.put(matcher.group(2), Integer.parseInt(matcher.group(1)));
                }
                bags.put(bagColor, subBags);
            }
            return bags;
        }
    }
}
