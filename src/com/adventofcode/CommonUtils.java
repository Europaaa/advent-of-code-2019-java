package com.adventofcode;

import java.net.URL;

public class CommonUtils {

    public static String getInputFile(String name) {
        Day1Part1 solver = new Day1Part1();

        URL resource = solver.getClass().getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("Input file not found.");
        }
        return resource.getFile();
    }
}
