package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day14Part2 {
    private static final String INPUT_FILE = "y2018/day14";

    private List<Integer> scores;
    private String board;

    private String target;
    private int n;

    private int player1;
    private int player2;

    public Day14Part2(int target) {
        this.scores = new ArrayList<>();
        this.scores.add(3);
        this.scores.add(7);

        this.board = "37";

        this.target = String.valueOf(target);
        this.n = this.target.length();

        this.player1 = 0;
        this.player2 = 1;
    }

    private int search() {
        int recipe1 = this.scores.get(this.player1);
        int recipe2 = this.scores.get(this.player2);

        int[] recipes = combine(recipe1, recipe2);

        for (int i = 0; i < recipes.length; i++) {
            this.scores.add(recipes[i]);

            this.board += recipes[i];
            if (this.board.length() > target.length()) {
                this.board = this.board.substring(this.board.length() - n);
            }

            // Using `equals` as opposed to `indexOf` gives a huge performance improvement
            if (this.board.equals(this.target)) {
                return this.scores.size() - n;
            }
        }

        this.player1 = (this.player1 + this.scores.get(this.player1) + 1) % this.scores.size();
        this.player2 = (this.player2 + this.scores.get(this.player2) + 1) % this.scores.size();

        return -1;
    }

    private int[] combine(int recipe1, int recipe2) {
        char[] chars = String.valueOf(recipe1 + recipe2).toCharArray();

        int[] recipes = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            recipes[i] = chars[i] - '0';
        }
        return recipes;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day14Part2 day14Part1 = new Day14Part2(getInput(CommonUtils.getInputFile(INPUT_FILE)));

        int index = -1;
        while (index == -1) {
            index = day14Part1.search();
        }
        System.out.println(index);
    }

    private static int getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextInt();
        }
    }
}
