package com.adventofcode.y2015;

import com.adventofcode.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14Part2 {

    private static final String INPUT_FILE = "y2015/day14";

    private static final Pattern PATTERN = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds\\.");

    private static long evaluate(Map<String, ReindeerData> reindeers, int seconds) {
        for (int i = 0; i < seconds; i++) {
            reindeers.values()
                    .forEach(reindeer -> {
                        if (reindeer.remainingSeconds == 0) {
                            reindeer.flying = !reindeer.flying;
                            reindeer.remainingSeconds = reindeer.flying ? reindeer.flySeconds : reindeer.restSeconds;
                        }

                        reindeer.remainingSeconds -= 1;
                        reindeer.distance += reindeer.flying ? reindeer.flySpeed : 0;
                    });

            long maxDistance = reindeers.values().stream()
                    .mapToLong(reindeer -> reindeer.distance)
                    .max()
                    .orElseThrow(IllegalStateException::new);

            reindeers.values().stream()
                    .filter(reindeer -> reindeer.distance == maxDistance)
                    .forEach(reindeer -> reindeer.score += 1);
        }

        return reindeers.values().stream()
                .mapToLong(reindeer -> reindeer.score)
                .max()
                .orElseThrow(IllegalStateException::new);
    }

    public static void main(String[] args) throws FileNotFoundException, JsonProcessingException {
        Map<String, ReindeerData> reindeers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(reindeers, 2503));
    }

    private static class ReindeerData {
        private int flySpeed;
        private int flySeconds;
        private int restSeconds;

        private boolean flying;
        private int remainingSeconds;
        private int distance;
        private int score;
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

                    data.flying = true;
                    data.remainingSeconds = data.flySeconds;
                    data.distance = 0;
                    data.score = 0;


                    reindeers.put(name, data);
                }
            }
            return reindeers;
        }
    }
}
