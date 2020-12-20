package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day21Part1 {

    private static final String INPUT_FILE = "y2020/day21";

    private static final Pattern PATTERN = Pattern.compile("((?:(?:\\w+)\\s)+)\\(contains ((?:(?:\\w+),\\s)*(?:\\w+))\\)");

    private List<Set<String>> ingredients;
    private List<Set<String>> allergens;

    private Map<String, Set<String>> possibleAllergens;

    public Day21Part1() {
        this.ingredients = new ArrayList<>();
        this.allergens = new ArrayList<>();

        this.possibleAllergens = new HashMap<>();
    }

    public Day21Part1 add(Set<String> ingredients, Set<String> allergens) {
        this.ingredients.add(ingredients);
        this.allergens.add(allergens);

        allergens.forEach(allergen -> {
            if (this.possibleAllergens.containsKey(allergen)) {
                this.possibleAllergens.get(allergen).retainAll(ingredients);
            } else {
                this.possibleAllergens.put(allergen, new HashSet<>(ingredients));
            }
        });
        return this;
    }

    public Map<String, String> reduce() {
        Map<String, String> mappings = new HashMap<>();
        Stack<String> ingredients = new Stack<>();

        possibleAllergens.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1)
                .peek(entry -> ingredients.add(entry.getValue().iterator().next()))
                .forEach(entry -> mappings.put(entry.getValue().iterator().next(), entry.getKey()));

        while (!ingredients.isEmpty()) {
            String ingredient = ingredients.pop();
            possibleAllergens.entrySet().stream()
                    .filter(entry -> !entry.getValue().equals(mappings.get(ingredient)))
                    .filter(entry -> entry.getValue().contains(ingredient))
                    .peek(entry -> entry.getValue().remove(ingredient))
                    .filter(entry -> entry.getValue().size() == 1)
                    .peek(entry -> ingredients.add(entry.getValue().iterator().next()))
                    .forEach(entry -> mappings.put(entry.getValue().iterator().next(), entry.getKey()));
        }

        return mappings;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day21Part1 day21Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Map<String, String> mappings = day21Part1.reduce();
        long count = day21Part1.ingredients.stream()
                .flatMap(Set::stream)
                .filter(ingredient -> !mappings.containsKey(ingredient))
                .count();

        System.out.println(count);
    }

    private static Day21Part1 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Day21Part1 day21Part1 = new Day21Part1();

            while (scanner.hasNext()) {
                Matcher matcher = PATTERN.matcher(scanner.nextLine());

                if (!matcher.matches()) {
                    continue;
                }

                Set<String> ingredients = Arrays.stream(matcher.group(1).split(" "))
                        .map(String::trim)
                        .collect(Collectors.toSet());

                Set<String> allergens = Arrays.stream(matcher.group(2).split(","))
                        .map(String::trim)
                        .collect(Collectors.toSet());

                day21Part1.add(ingredients, allergens);
            }
            return day21Part1;
        }
    }
}
