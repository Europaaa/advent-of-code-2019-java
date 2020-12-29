package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3Part2 {
    private static final String INPUT_FILE = "y2018/day3";

    private static final Pattern PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

    public static int calculate(Area[] areas) {
        for (int i = 0; i < areas.length; i++) {
            boolean intact = true;
            for (int j = 0; j < areas.length; j++) {
                if (i == j) {
                    continue;
                }

                if (areas[i].overlaps(areas[j])) {
                    intact = false;
                    break;
                }
            }

            if (intact) {
                return areas[i].id;
            }
        }
        throw new IllegalStateException();
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Area> areas = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int id = calculate(areas.toArray(new Area[0]));
        System.out.println(id);
    }

    private static class Area {
        private int id;
        private int x;
        private int y;
        private int w;
        private int h;

        public Area(int id, int x, int y, int w, int h) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public boolean overlaps(Area area) {
            return this.x <= area.x + area.w && area.x <= this.x + this.w &&
                    this.y <= area.y + area.h && area.y <= this.y + this.h;
        }
    }

    private static List<Area> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Area> areas = new ArrayList<>();
            while (scanner.hasNext()) {
                Matcher matcher = PATTERN.matcher(scanner.nextLine());
                if (!matcher.matches()) {
                    continue;
                }

                areas.add(new Area(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)),
                        Integer.parseInt(matcher.group(5))));
            }
            return areas;
        }
    }
}
