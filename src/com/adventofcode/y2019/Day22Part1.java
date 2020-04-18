package com.adventofcode.y2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day22Part1 {

    private static final String INPUT_FILE = "y2019/day22";

    public static int getCardPosition(int cards, int target, List<String> actions) {
        int position = target;
        for (String action : actions) {
            if (action.startsWith("cut")) {
                int n = Integer.valueOf(action.split("cut ")[1]);
                position = getPositionForCut(cards, n, position);
            } else if (action.startsWith("deal with increment")) {
                int n = Integer.valueOf(action.split("deal with increment ")[1]);
                position = getPositionForDealIncrement(cards, n, position);
            } else {
                position = getPositionForDeal(cards, position);
            }
        }
        return position;
    }

    private static int getPositionForCut(int cards, int n, int position) {
        if (n < 0) {
            n = cards + n;
        }

        if (position < n) {
            return cards - (n - position);
        }
        return position - n;
    }

    private static int getPositionForDealIncrement(int cards, int n, int position) {
        return (position * n) % cards;
    }

    private static int getPositionForDeal(int cards, int position) {
        return cards - position - 1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<String> actions = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int position = getCardPosition(10007, 2019, actions);
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
