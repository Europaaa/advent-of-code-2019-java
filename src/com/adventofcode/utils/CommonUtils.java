package com.adventofcode.utils;

import java.net.URL;

public class CommonUtils {

    public static String getInputFile(String name) {
        CommonUtils solver = new CommonUtils();

        URL resource = solver.getClass().getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("Input file not found.");
        }
        return resource.getFile();
    }
}
