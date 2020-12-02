package com.adventofcode.y2015;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9Part1 {

    private static final String INPUT_FILE = "y2015/day9";

    private static final Pattern REGEX = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");

    public static long evaluate(Map<String, Map<String, Long>> distances) {
        Set<String> cities = new HashSet<>(distances.keySet());

        long minDistance = Long.MAX_VALUE;
        for (String city : cities) {
            minDistance = Math.min(minDistance, visit(city, distances, cities));
        }
        return minDistance;
    }

    private static long visit(String city, Map<String, Map<String, Long>> distances, Set<String> targets) {
        // If we have visited all cities, no need to travel anymore
        if (targets.isEmpty()) {
            return 0;
        }

        // If the city is the last target city, no need to travel anymore
        if (targets.size() == 1 && targets.contains(city)) {
            return 0;
        }

        // If we still need to visit more cities but we can't go from the current city, return -1
        if (!distances.containsKey(city)) {
            return -1;
        }

        // Consider the current city as visited
        Set<String> newTargets = new HashSet<>(targets);
        newTargets.remove(city);

        Long minDistance = null;

        Map<String, Long> destinations = distances.get(city);
        for (String destination : destinations.keySet()) {
            if (!newTargets.contains(destination)) {
                continue;
            }
            long distance = visit(destination, distances, newTargets);
            if (distance < 0) {
                continue;
            }
            if (minDistance == null) {
                minDistance = distance + destinations.get(destination);
            } else {
                minDistance = Math.min(minDistance, distance + destinations.get(destination));
            }
        }
        return minDistance == null ? -1 : minDistance;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Map<String, Long>> distances = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(distances));
    }

    private static Map<String, Map<String, Long>> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {

            Map<String, Map<String, Long>> distances = new HashMap<>();
            while (scanner.hasNext()) {
                String code = scanner.nextLine();

                Matcher matcher = REGEX.matcher(code);
                if (matcher.matches()) {
                    String city1 = matcher.group(1);
                    String city2 = matcher.group(2);
                    Long distance = Long.valueOf(matcher.group(3));

                    Map<String, Long> destinations1 = distances.getOrDefault(city1, new HashMap<>());
                    destinations1.put(city2, distance);
                    distances.put(city1, destinations1);

                    Map<String, Long> destinations2 = distances.getOrDefault(city2, new HashMap<>());
                    destinations2.put(city1, distance);
                    distances.put(city2, destinations2);
                }
            }
            return distances;
        }
    }

    private static class Edge {
        private String vertex1;
        private String vertex2;

        private Edge(String vertex1, String vertex2) {
            if (vertex1.compareTo(vertex2) > 0) {
                this.vertex1 = vertex1;
                this.vertex2 = vertex2;
            } else {
                this.vertex1 = vertex2;
                this.vertex2 = vertex1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(vertex1, edge.vertex1) &&
                    Objects.equals(vertex2, edge.vertex2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertex1, vertex2);
        }
    }
}
