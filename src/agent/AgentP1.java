package agent;

import util.Game;

public class AgentP1 {

    private Game game;
    
    public AgentP1(Game game) {
        this.game = game;
    }

    public void run(boolean verbose) {
        while(!game.isWon() && !game.isLost()) {
            if(verbose) {
                game.printBoard(game.getView());
            }
            int[] position = getNextProbe(game.getView());
            if(position != null) {
                game.probe(position[0], position[1]);
            }
        }
        game.printFinalMap();
    }

    private int[] getNextProbe(char[][] view) {
        for(int i = 0; i < view.length; i++) {
            for(int j = 0; j < view[0].length; j++) {
                if(view[i][j] == '?') {
                    return  new int[] {j, i};
                }
            }
        }
        return null;
    }
}
