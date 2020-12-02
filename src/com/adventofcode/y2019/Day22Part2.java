package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day22Part2 {

    private static final String INPUT_FILE = "y2019/day22";

    public static long getCardAtPosition(long cards, long target, String[] actions, long repeats) {
        long position = target;

        for (int r = 0; r < repeats; r++) {
            for (int i = actions.length - 1; i >= 0; i--) {
                String action = actions[i];

                if (action.startsWith("cut")) {
                    // Re-doing a cut is equivalent of cutting an opposite number of cards
                    long n = Long.valueOf(action.split("cut ")[1]);
                    position = getPositionForCut(cards, -n, position);
                } else if (action.startsWith("deal with increment")) {
                    long n = Long.valueOf(action.split("deal with increment ")[1]);
                    position = getPositionForDealIncrement(cards, cards - n, position);
                } else {
                    position = getPositionForDeal(cards, position);
                }
            }
        }
        return position;
    }

    private static long getPositionForCut(long cards, long n, long position) {
        if (n < 0) {
            n = cards + n;
        }

        if (position < n) {
            return cards - (n - position);
        }
        return position - n;
    }

    private static long getPositionForDealIncrement(long cards, long n, long position) {
        BigDecimal product = new BigDecimal(position).multiply(new BigDecimal(n));
        BigDecimal remainder = product.remainder(new BigDecimal(cards));
        return remainder.longValue();
    }

    private static long getPositionForDeal(long cards, long position) {
        return cards - position - 1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> actions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long position = getCardAtPosition(119315717514047l, 2020l, actions.toArray(new String[0]), 101741582076661l);
        System.out.println(position);
    }

    private static List<String> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<String> mass = new ArrayList<>();
            while (scanner.hasNext()) {
                mass.add(scanner.nextLine());
            }
            return mass;
        }
    }
}
