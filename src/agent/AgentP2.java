package agent;

import util.Game;
import util.SPS;

import java.util.ArrayList;

public class AgentP2 implements SPS {
    private Game game;

    public AgentP2(Game game) {
        this.game = game;
    }

    public void run(boolean verbose) {
        int IndexOfNextSafeCell = 0;
        int[] nextFlag = null;
        int[] nextProbed = game.getSafeCells(IndexOfNextSafeCell++);
        while(nextFlag != null || nextProbed != null ) {
            if(verbose) {
                game.printBoard(game.getView());
            }
            if(nextFlag != null) {
                game.flag(nextFlag[0], nextFlag[1]);
            } else if (nextProbed != null) {
                game.probe(nextProbed[0], nextProbed[1]);
            }

            nextFlag = getNextFlag(game.getView());
            nextProbed = getNextProbed(game.getView());
            if(nextProbed == null) {
                nextProbed = game.getSafeCells(IndexOfNextSafeCell++);
            }
        }
        game.printFinalMap();
    }

    private int[] getNextFlag(char[][] view) {
        for(int i = 0; i < view.length; i++) {
            for(int j = 0; j < view[0].length; j++) {
                if(view[i][j] > '0' && view[i][j] <= '6') {
                    int bombs = view[i][j] - '0';
                    ArrayList<int[]> AMNs = countAMN(j, i, view);
                    ArrayList<int[]> AFNs = countAFN(j, i, view);
                    if ( (bombs - AMNs.size()) == AFNs.size() && AFNs.size() != 0) {
                        return AFNs.get(0);
                    }
                }

            }
        }
        return null;
    }

    private int[] getNextProbed(char[][] view) {
        for(int i = 0; i < view.length; i++) {
            for(int j = 0; j < view[0].length; j++) {
                if(view[i][j] > '0' && view[i][j] <= '6') {
                    int bombs = view[i][j] - '0';
                    ArrayList<int[]> AMNs = countAMN(j, i, view);
                    ArrayList<int[]> AFNs = countAFN(j, i, view);
                    if (bombs == AMNs.size() && AFNs.size() > 0) {
                        return AFNs.get(0);
                    }
                }

            }
        }
        return null;
    }

}
