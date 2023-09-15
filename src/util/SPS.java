package util;

import java.util.ArrayList;

public interface SPS {
    default ArrayList<int[]> countAFN(int x, int y, char[][] view) {
        ArrayList<int[]> AFNs = new ArrayList<>();
        for(int i = 0; i < Game.neighbors.length; i++) {
            int neighborX = x + Game.neighbors[i][0];
            int neighborY = y + Game.neighbors[i][1];
            if(neighborX >= 0 && neighborX < view[0].length &&
                    neighborY >= 0 && neighborY < view.length &&
                    view[neighborY][neighborX] == '?') {
                AFNs.add(new int[] {neighborX, neighborY});
            }
        }
        return AFNs;
    }

    default ArrayList<int[]> countAMN(int x, int y, char[][] view) {
        ArrayList<int[]> AMNs = new ArrayList<>();
        for(int i = 0; i < Game.neighbors.length; i++) {
            int neighborX = x + Game.neighbors[i][0];
            int neighborY = y + Game.neighbors[i][1];
            if(neighborX >= 0 && neighborX < view.length &&
                    neighborY >= 0 && neighborY < view[0].length &&
                    view[neighborY][neighborX] == '*') {
                AMNs.add(new int[] {neighborX, neighborY});
            }
        }
        return AMNs;
    }
}
