package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4Part2 {

    private static final String INPUT_FILE = "y2020/day4";

    private static final String PATTERN_BYR = "(?:(byr):(?:(?:19[^1]\\d)|(?:200[12])))";
    private static final String PATTERN_ECL = "(?:(ecl):(?:(?:amb)|(?:blu)|(?:brn)|(?:hzl)|(?:grn)|(?:gry)|(?:oth)))";
    private static final String PATTERN_EYR = "(?:(eyr):(?:(?:202\\d)|(?:2030)))";
    private static final String PATTERN_HCL = "(?:(hcl):#[0-9a-f]{6})";
    private static final String PATTERN_HGT = "(?:(hgt):(?:(?:1[5-8]\\dcm)|(?:19[0-3]cm)|(?:59in)|(?:6\\din)|(?:7[0-6]in)))";
    private static final String PATTERN_IRY = "(?:(iyr):(?:(?:201\\d)|(?:2020)))";
    private static final String PATTERN_PID = "(?:(pid):\\d{9})";

    private static final Pattern PATTERN = Pattern.compile("(" + String.join("|", PATTERN_BYR, PATTERN_ECL, PATTERN_EYR,
            PATTERN_HCL, PATTERN_HGT, PATTERN_IRY, PATTERN_PID) + ")");

    public static boolean isValid(String passport) {
        Matcher matcher = PATTERN.matcher(passport);

        Set<String> fields = new HashSet<>();
        while (matcher.find()) {
            fields.add(matcher.group(1));
        }
        return fields.size() == 7;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> passports = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = passports.stream().filter(Day4Part2::isValid).count();
        System.out.println(result);
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> passports = new ArrayList<>();
            while (scanner.hasNext()) {
                StringBuilder sb = new StringBuilder();

                String line = scanner.nextLine().trim();
                while (!line.isEmpty() && scanner.hasNext()) {
                    sb.append(line).append(" ");
                    line = scanner.nextLine().trim();
                }

                sb.append(line);
                passports.add(sb.toString().trim());
            }
            return passports;
        }
    }
}
