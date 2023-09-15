package agent;

import org.sat4j.core.VecInt;
import util.Game;
import util.KnowledgeBaseCNF;

public class AgentP4 {
    private Game game;
    private KnowledgeBaseCNF KB;

    public AgentP4(Game game) {
        this.game = game;
        init(game.getView());
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

            KB.update(game.getView());

            nextFlag = getNextFlag(game.getView());
            nextProbed = getNextProbed(game.getView());
            if(nextProbed == null) {
                nextProbed = game.getSafeCells(IndexOfNextSafeCell++);
            }
        }
        game.printFinalMap();
    }

    private int[] getNextProbed(char[][] view) {
        for(int i = 0; i < view.length; i++) {
            for(int j = 0; j < view[0].length; j++) {
                if(view[i][j] == '?') {
                    int variable = KB.flatten(j, i, view);
                    int[] clause = {variable};
                    VecInt vecInt = new VecInt(clause);
                    if(KB.getVariables().contains(variable) && !KB.isSatisfactory(vecInt)){
                        return new int[] {j, i};
                    }
                }
            }
        }
        return null;
    }

    private int[] getNextFlag(char[][] view) {
        for(int i = 0; i < view.length; i++) {
            for(int j = 0; j < view[0].length; j++) {
                if(view[i][j] == '?') {
                    int variable = KB.flatten(j, i, view);
                    int[] clause = {-variable};
                    VecInt vecInt = new VecInt(clause);
                    if(KB.getVariables().contains(variable) && !KB.isSatisfactory(vecInt)){
                        return new int[] {j, i};
                    }
                }
            }
        }
        return null;
    }


    private void init(char[][] view) {
        KB = new KnowledgeBaseCNF(view);
    }
}
