package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4Part1 {
    private static final String INPUT_FILE = "y2018/day4";

    private static final Pattern PATTERN_GUARD = Pattern.compile("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}] Guard #(\\d+) begins shift");
    private static final Pattern PATTERN_SLEEP = Pattern.compile("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:(\\d{2})] falls asleep");
    private static final Pattern PATTERN_WAKE = Pattern.compile("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:(\\d{2})] wakes up");

    public static long calculate(List<String> logs) {
        Map<String, List<TimeRange>> history = parse(logs);

        // Find the guard that has the most minutes asleep.
        String guard = history.keySet().stream()
                .max(Comparator.comparing(g -> history.get(g).stream()
                        .mapToInt(tr -> tr.end - tr.start)
                        .sum()))
                .get();

        // What minute does that guard spend asleep the most?
        long[] counters = new long[60];
        Arrays.fill(counters, 0);

        List<TimeRange> timeRanges = history.get(guard);
        for (TimeRange timeRange : timeRanges) {
            for (int i = timeRange.start; i < timeRange.end; i++) {
                counters[i] += 1;
            }
        }

        int max = 0;
        for (int i = 1; i < counters.length; i++) {
            if (counters[i] > counters[max]) {
                max = i;
            }
        }
        return Long.parseLong(guard) * max;
    }

    private static Map<String, List<TimeRange>> parse(List<String> logs) {
        Map<String, List<TimeRange>> history = new HashMap<>();

        String guard = null;
        int start = -1;

        for (String log : logs) {
            Matcher matcher = PATTERN_GUARD.matcher(log);
            if (matcher.matches()) {
                guard = matcher.group(1);
                continue;
            }

            matcher = PATTERN_SLEEP.matcher(log);
            if (matcher.matches()) {
                start = Integer.parseInt(matcher.group(1));
                continue;
            }

            matcher = PATTERN_WAKE.matcher(log);
            if (!matcher.matches()) {
                continue;
            }

            if (!history.containsKey(guard)) {
                history.put(guard, new ArrayList<>());
            }
            history.get(guard).add(new TimeRange(start, Integer.parseInt(matcher.group(1))));
        }

        return history;
    }

    public static class TimeRange {
        private int start;
        private int end;

        public TimeRange(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> logs = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long result = calculate(logs);
        System.out.println(result);
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> logs = new ArrayList<>();
            while (scanner.hasNext()) {
                logs.add(scanner.nextLine());
            }

            logs.sort(Comparator.naturalOrder());
            return logs;
        }
    }
}
