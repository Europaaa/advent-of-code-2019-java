package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day17Part1 {

    private static final String INPUT_FILE = "y2020/day17";

    public static class State {

        private char[][][] state;

        private int dimX;
        private int dimY;
        private int dimZ;

        public State(char[][][] state) {
            this.state = state;

            this.dimZ = state.length;
            this.dimY = state[0].length;
            this.dimX = state[0][0].length;
        }

        public State expand() {
            char[][][] newState = new char[dimZ + 2][][];

            for (int z = 0; z < dimZ + 2; z++) {
                newState[z] = new char[dimY + 2][];

                for (int y = 0; y < dimY + 2; y++) {
                    newState[z][y] = new char[dimX + 2];

                    for (int x = 0; x < dimX + 2; x++) {
                        newState[z][y][x] = getNextState(state, x - 1, y - 1, z - 1);
                    }
                }
            }
            return new State(newState);
        }

        private static char getNextState(char[][][] space, int x, int y, int z) {
            // Count active neighbors
            int count = 0;
            for (int i = -1; i <= 1; i++) {
                if (z + i < 0 || z + i >= space.length) {
                    continue;
                }

                for (int j = -1; j <= 1; j++) {
                    if (y + j < 0 || y + j >= space[z + i].length) {
                        continue;
                    }

                    for (int k = -1; k <= 1; k++) {
                        if (x + k < 0 || x + k >= space[z + i][y + j].length) {
                            continue;
                        }
                        if (i == 0 && j == 0 && k == 0) {
                            continue;
                        }
                        if (space[z + i][y + j][x + k] == '#') {
                            count += 1;
                        }
                    }
                }
            }

            // Calculate the next state
            char current;
            if (0 <= z && z < space.length && 0 <= y && y < space[z].length && 0 <= x && x < space[z][y].length) {
                current = space[z][y][x];
            } else {
                current = '.';
            }

            switch (current) {
                case '#':
                    if (count == 2 || count == 3) {
                        return  '#';
                    }
                    return  '.';
                case '.':
                    if (count == 3) {
                        return  '#';
                    }
                    return '.';
            }
            throw new IllegalStateException();
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();

            for (int z = 0; z < dimZ; z++) {
                builder.append("z=").append((-1 * ((dimZ - 1) / 2) + z)).append("\n");

                for (int y = 0; y < dimY; y++) {
                    for (int x = 0; x < dimX; x++) {
                        builder.append(state[z][y][x]);
                    }
                    builder.append("\n");
                }

                builder.append("\n");
            }

            builder.append("\n");
            return builder.toString();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        State state = getInput(CommonUtils.getInputFile(INPUT_FILE));

        for (int i = 0; i < 6; i++) {
            state = state.expand();
        }

        long count = 0;
        for (int i = 0; i < state.state.length; i++) {
            for (int j = 0; j < state.state[i].length; j++) {
                for (int k = 0; k < state.state[i][j].length; k++) {
                    if (state.state[i][j][k] == '#') {
                        count += 1;
                    }
                }
            }
        }
        System.out.println(count);
    }

    private static State getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            char[][][] space = new char[1][][];

            List<char[]> lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine().toCharArray());
            }

            space[0] = lines.toArray(new char[0][]);
            return new State(space);
        }
    }
}
