package com.adventofcode.y2019;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day8Part1 {

    private static final String INPUT_FILE = "y2019/day8";

    private static final int IMG_WIDTH = 25;
    private static final int IMG_HEIGHT = 6;

    public static long getChecksum(List<int[][]> layers) {
        int iLayer = 0;

        long minCount = Long.MAX_VALUE;
        for (int i = 0; i < layers.size(); i++) {
            long count = count(layers.get(i), 0);
            if (minCount > count) {
                minCount = count;
                iLayer = i;
            }
        }

        int[][] layer = layers.get(iLayer);
        return count(layer, 1) * count(layer, 2);
    }

    private static long count(int[][] layer, int target) {
        return Arrays.stream(layer).flatMapToInt(Arrays::stream).filter(i -> i == target).count();
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<int[][]> layers = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long checksum = getChecksum(layers);
        System.out.println(checksum);
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
