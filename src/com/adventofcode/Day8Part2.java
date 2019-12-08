package com.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day8Part2 {

    private static final String INPUT_FILE = "day8";

    private static final int IMG_WIDTH = 25;
    private static final int IMG_HEIGHT = 6;

    private static final int COLOR_BLACK = 0;
    private static final int COLOR_WHITE = 1;
    private static final int COLOR_TRANSPARENT = 2;

    public static int[][] decodeImage(List<int[][]> layers) {
        int[][] image = new int[IMG_HEIGHT][IMG_WIDTH];

        for (int row = 0; row < IMG_HEIGHT; row++) {
            for (int column = 0; column < IMG_WIDTH; column++) {
                int r = row;
                int c = column;

                image[r][c] = layers.stream()
                        .map(layer -> layer[r][c])
                        .filter(color -> color != COLOR_TRANSPARENT)
                        .findFirst().orElse(COLOR_TRANSPARENT);
            }
        }
        return image;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<int[][]> layers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int[][] image = decodeImage(layers);

        for (int row = 0; row < IMG_HEIGHT; row++) {
            for (int column = 0; column < IMG_WIDTH; column++) {
                char pixel = (image[row][column] == COLOR_BLACK) ? ' ' : 'X';
                System.out.print(pixel);
            }
            System.out.println();
        }
    }

    private static List<int[][]> getInput(String path) throws FileNotFoundException {
        List<int[][]> layers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.useDelimiter("");

            while (scanner.hasNext()) {
                int[][] layer = new int[IMG_HEIGHT][IMG_WIDTH];
                for (int i = 0; i < IMG_HEIGHT; i++) {
                    for (int j = 0; j < IMG_WIDTH; j++) {
                        layer[i][j] = scanner.nextInt();
                    }
                }
                layers.add(layer);
            }
            return layers;
        }
    }
}
