package com.adventofcode.y2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day18Part1Temp {

    private static final String INPUT_FILE = "y2019/day18";

    static class Explorer {

        private static final char CELL_WALL = '#';
        private static final char CELL_ENTRANCE = '@';

        private char[][] map;
        private Position pointer;
        private Map<Character, Position> keysGained;
        private Set<Position> doorsOpened;

        Explorer(char[][] map) {
            this.map = map;
            this.keysGained = new HashMap<>();
            this.doorsOpened = new HashSet<>();

            this.pointer = getPointerPosition(map);
        }

        private Position getPointerPosition(char[][] map) {
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (map[y][x] == CELL_ENTRANCE) {
                        return new Position(x, y);
                    }
                }
            }
            throw new IllegalArgumentException();
        }

        public int explore() {
            Map<Position, Integer> keyOptions = getKeyOptions(this.pointer, 0);
            Map<Position, Integer> doorOptions = getDoorOptions(this.pointer, 0);

            // If there's no keys anymore we are done
            if (keyOptions.isEmpty() && doorOptions.isEmpty()) {
                return 0;
            }

            Map<Position, Integer> openDoorOptions = doorOptions.keySet().stream()
                    .filter(this::canOpenDoor)
                    .collect(Collectors.toMap(Function.identity(), doorOptions::get));

            Position pointer = this.pointer;

            List<Integer> steps = new ArrayList<>();

            // Explore all the key options
            for (Position marker : keyOptions.keySet()) {
                char c = this.map[marker.y][marker.x];

                this.keysGained.put(c, marker);
                this.pointer = marker;

                steps.add(this.explore() + keyOptions.get(marker));

                this.keysGained.remove(c);
                this.pointer = pointer;
            }

            // Explore all the open door options
            for (Position marker : openDoorOptions.keySet()) {
                this.doorsOpened.add(marker);
                this.pointer = marker;

                steps.add(this.explore() + doorOptions.get(marker));

                this.doorsOpened.remove(marker);
                this.pointer = pointer;
            }
            return steps.stream().mapToInt(i -> i).min().getAsInt();
        }

        private Map<Position, Integer> getKeyOptions(Position position, int steps) {
            Map<Position, Integer> options = new HashMap<>();

            Queue<Position> queue = new LinkedList<>();
            queue.offer(position);

            Set<Position> visited = new HashSet<>();

            while (!queue.isEmpty()) {
                int size = queue.size();

                for (int i = 0; i < size; i++) {
                    Position marker = queue.poll();

                    if (!isValidOption(marker) || visited.contains(marker) || !isValidOptionForKey(marker)) {
                        continue;
                    }

                    visited.add(marker);

                    if (isKeyOption(marker)) {
                        options.put(marker, steps);
                        continue;
                    }

                    queue.offer(new Position(marker.x, marker.y - 1));
                    queue.offer(new Position(marker.x, marker.y + 1));
                    queue.offer(new Position(marker.x - 1, marker.y));
                    queue.offer(new Position(marker.x + 1, marker.y));
                }

                steps += 1;
            }
            return options;
        }


        private Map<Position, Integer> getDoorOptions(Position position, int steps) {
            Map<Position, Integer> options = new HashMap<>();

            Queue<Position> queue = new LinkedList<>();
            queue.offer(position);

            Set<Position> visited = new HashSet<>();

            while (!queue.isEmpty()) {
                int size = queue.size();

                for (int i = 0; i < size; i++) {
                    Position marker = queue.poll();

                    if (!isValidOption(marker) || visited.contains(marker) || !isValidOptionForDoor(marker)) {
                        continue;
                    }

                    visited.add(marker);

                    if (isDoorOption(marker)) {
                        options.put(marker, steps);
                        continue;
                    }

                    queue.offer(new Position(marker.x, marker.y - 1));
                    queue.offer(new Position(marker.x, marker.y + 1));
                    queue.offer(new Position(marker.x - 1, marker.y));
                    queue.offer(new Position(marker.x + 1, marker.y));
                }

                steps += 1;
            }
            return options;
        }

        private boolean isValidOptionForKey(Position position) {
            return !isWall(position) && !isDoorOption(position);
        }

        private boolean isValidOptionForDoor(Position position) {
            return !isWall(position) && !isKeyOption(position);
        }

        private boolean isValidOption(Position position) {
            return position.x >= 0 && position.x < this.map[0].length && position.y >= 0 && position.y < this.map.length;
        }

        private boolean isWall(Position position) {
            char c = this.map[position.y][position.x];
            return c == CELL_WALL;
        }

        private boolean isKeyOption(Position position) {
            char c = this.map[position.y][position.x];
            return Character.isLowerCase(c) && !this.keysGained.containsKey(c);
        }

        private boolean isDoorOption(Position position) {
            char c = this.map[position.y][position.x];
            return Character.isUpperCase(c) && !this.doorsOpened.contains(position);
        }

        private boolean canOpenDoor(Position position) {
            char c = this.map[position.y][position.x];
            return this.keysGained.containsKey(Character.toLowerCase(c));
        }
    }

    static class Position {
        final int x;
        final int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            Position other = (Position) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        char[][] map = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Explorer explorer = new Explorer(map);
        System.out.println(explorer.explore());
    }

    private static char[][] getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<char[]> lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine().toCharArray());
            }
            return lines.toArray(new char[0][]);
        }
    }
}
