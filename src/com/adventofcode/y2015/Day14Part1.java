package com.adventofcode.y2015;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14Part1 {

    private static final String INPUT_FILE = "y2015/day14";

    private static final Pattern PATTERN = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds\\.");

    private static long evaluate(Map<String, ReindeerData> reindeers, int seconds) {
        return reindeers.values().stream()
                .mapToLong(data -> evaluate(data, seconds))
                .max()
                .orElseThrow(IllegalStateException::new);
    }

    private static long evaluate(ReindeerData reindeer, int seconds) {
        int repSeconds = reindeer.flySeconds + reindeer.restSeconds;
        int reps = seconds / repSeconds;

        int remainingSeconds = seconds % repSeconds;
        if (remainingSeconds > reindeer.flySeconds) {
            remainingSeconds = reindeer.flySeconds;
        }
        return reindeer.flySpeed * (reps * reindeer.flySeconds + remainingSeconds);
    }

    public static void main(String[] args) throws FileNotFoundException, JsonProcessingException {
        Map<String, ReindeerData> reindeers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(reindeers, 2503));
    }

    private static class ReindeerData {
        private int flySpeed;
        private int flySeconds;
        private int restSeconds;
    }

    private static Map<String, ReindeerData> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {

            Map<String, ReindeerData> reindeers = new HashMap<>();
            while (scanner.hasNext()) {
                Matcher matcher = PATTERN.matcher(scanner.nextLine());
                if (matcher.matches()) {
                    String name = matcher.group(1);

                    ReindeerData data = new ReindeerData();
                    data.flySpeed = Integer.valueOf(matcher.group(2));
                    data.flySeconds = Integer.valueOf(matcher.group(3));
                    data.restSeconds = Integer.valueOf(matcher.group(4));

                    reindeers.put(name, data);
                }
            }
            return reindeers;
        }
    }
}
