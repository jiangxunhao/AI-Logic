package util;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.formulas.Variable;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;


import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class KnowledgeBaseDNF implements SPS, Combination {
    private List<Formula> formulas;
    private SortedSet<Variable> variables;
    private FormulaFactory formulaFactory;

    public KnowledgeBaseDNF() {
        init();
    }

    public void update(char[][] view) {
        formulas.clear();
        variables.clear();
        for(int i = 0; i < view.length; i++) {
            for(int j = 0; j < view[0].length; j++) {
                if(view[i][j] >= '1' && view[i][j] <= '6') {
                    int bombs = view[i][j] - '0';
                    addNeighborFormula(bombs, j, i, view);
                }
            }
        }
    }

    public Tristate isSatisfactory(Literal literal) {
        SATSolver miniSat = MiniSat.miniSat(formulaFactory);
        miniSat.add(formulas);
        return miniSat.sat(literal);
    }

    private void addNeighborFormula(int bombs, int x, int y, char[][] view) {
        List<List<Boolean>> cases = new ArrayList<>();
        List<Boolean> situation = new ArrayList<>();
        ArrayList<int[]> AFNs = countAFN(x, y, view);
        ArrayList<int[]> AMNs = countAMN(x, y, view);
        getCases(cases, situation, 0, bombs-AMNs.size(), AFNs.size());

        List<Formula> neighborFormulas = new ArrayList<>();
        for(List<Boolean> sit : cases) {
            List<Formula> neighborFormula1 = new ArrayList<>();
            for(int i = 0; i < sit.size(); i++) {
                int[] position = AFNs.get(i);
                String variableString = toVariable(position[0], position[1]);
                Variable variable = formulaFactory.variable(variableString);
                variables.add(variable);

                Formula elementFormula = formulaFactory.literal(variableString, sit.get(i));
                neighborFormula1.add(elementFormula);

            }

            Formula neighborFormula = formulaFactory.and(neighborFormula1);
            neighborFormulas.add(neighborFormula);

        }

        Formula formula = formulaFactory.or(neighborFormulas);
        formulas.add(formula);

    }

    private void init() {
        formulas = new ArrayList<>();
        variables = new TreeSet<>();
        formulaFactory = new FormulaFactory();
    }

    public static String toVariable(int x, int y) {
        return new String("D" + x + y);
    }

    public SortedSet<Variable> getVariables() {
        return variables;
    }
}
