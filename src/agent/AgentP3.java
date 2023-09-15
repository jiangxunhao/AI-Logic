package agent;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.formulas.Variable;
import util.Game;
import util.KnowledgeBaseDNF;

public class AgentP3 {
    private Game game;
    private KnowledgeBaseDNF KB;

    public AgentP3(Game game) {
        this.game = game;
        init();
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
                FormulaFactory f = new FormulaFactory();
                String variableString = KnowledgeBaseDNF.toVariable(j, i);
                Variable variable = f.variable(variableString);

                if(view[i][j] == '?' && KB.getVariables().contains(variable)) {
                    Literal l = f.literal(variableString, true);
                    if( KB.isSatisfactory(l) == Tristate.FALSE ){
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
                FormulaFactory f = new FormulaFactory();
                String variableString = KnowledgeBaseDNF.toVariable(j, i);
                Variable variable = f.variable(variableString);

                if(view[i][j] == '?' && KB.getVariables().contains(variable)) {
                    Literal l = f.literal(variableString, false);
                    if( KB.isSatisfactory(l) == Tristate.FALSE ){
                        return new int[] {j, i};
                    }
                }
            }
        }
        return null;
    }

    private void init() {
        KB = new KnowledgeBaseDNF();
    }

}
