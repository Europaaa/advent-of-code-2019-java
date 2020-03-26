package com.adventofcode.y2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day14Part1 {

    private static final String INPUT_FILE = "y2019/day14";

    private static final String TARGET = "FUEL";
    private static final String SOURCE = "ORE";

    public static int getQuantity(Potion target, List<Reaction> reactions) {
        Map<String, Reaction> map = reactions.stream()
                .collect(Collectors.toMap(reaction -> reaction.target.chemical, Function.identity()));

        Map<String, Integer> leftovers = new HashMap<>();
        return getQuantity(target, map, leftovers);
    }

    private static int getQuantity(Potion target, Map<String, Reaction> reactions, Map<String, Integer> leftovers) {
        if (SOURCE.equals(target.chemical)) {
            return target.quantity;
        }

        // Check if we can generate the target using leftovers
        if (leftovers.getOrDefault(target.chemical, 0) >= target.quantity) {
            leftovers.put(target.chemical, leftovers.getOrDefault(target.chemical, 0) - target.quantity);
            return 0;
        }

        // Update the quantity using leftovers
        int quantity = target.quantity - leftovers.getOrDefault(target.chemical, 0);
        leftovers.put(target.chemical, 0);

        // Get target from reactions
        Reaction reaction = reactions.get(target.chemical);

        int multiplier = 1;
        if (reaction.target.quantity < quantity) {
            multiplier = quantity / reaction.target.quantity + (quantity % reaction.target.quantity > 0 ? 1 : 0);
        }

        int count = 0;
        for (Potion source : reaction.sources) {
            Potion potion = new Potion(source.chemical, source.quantity * multiplier);
            count += getQuantity(potion, reactions, leftovers);
        }

        int leftover = reaction.target.quantity * multiplier - quantity;
        leftovers.put(target.chemical, leftovers.getOrDefault(target.chemical, 0) + leftover);

        return count;
    }

    static class Potion {
        private String chemical;
        private int quantity;

        Potion(String chemical, int quantity) {
            this.chemical = chemical;
            this.quantity = quantity;
        }
    }

    static class Reaction {
        private Potion target;
        private List<Potion> sources;

        Reaction(Potion target, List<Potion> sources) {
            this.target = target;
            this.sources = sources;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Reaction> reactions = getInput(CommonUtils.getInputFile(INPUT_FILE));
        System.out.println(getQuantity(new Potion(TARGET, 1), reactions));
    }

    private static List<Reaction> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {

            List<Reaction> reactions = new ArrayList<>();
            while (scanner.hasNext()) {
                String[] tokens = scanner.nextLine().split("=>");

                List<Potion> potions = Arrays.stream(tokens[0].trim().split(","))
                        .map(String::trim)
                        .map(Day14Part1::getPotion)
                        .collect(Collectors.toList());

                reactions.add(new Reaction(getPotion(tokens[1].trim()), potions));
            }
            return reactions;
        }
    }

    private static Potion getPotion(String token) {
        String[] tokens = token.split(" ");
        return new Potion(tokens[1].trim(), Integer.valueOf(tokens[0].trim()));
    }
}
