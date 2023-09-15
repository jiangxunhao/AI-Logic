package util;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.*;

import java.util.*;

public class KnowledgeBaseCNF implements SPS, Combination {
    static public int SAFE_VAR_NUMBER = 5;
    static public int SAFE_CLAUSE_NUMBER = 20;
    static int MAXVAR;
    static int NBCLAUSES;
    private IVec<IVecInt> formulas;
    private SortedSet<Integer> variables;


    public KnowledgeBaseCNF(char[][] view) {
        init(view);
    }

    public void update(char[][] view) {
        formulas.clear();
        variables.clear();
        for(int i = 0; i < view.length; i++) {
            for(int j = 0; j < view[0].length; j++) {

                if(view[i][j] >= '1' && view[i][j] <= '6') {
                    ArrayList<int[]> AFNs = countAFN(j, i, view);
                    ArrayList<int[]> AMNs = countAMN(j, i, view);
                    int bombs = view[i][j] - '0';
                    addExact(AFNs, bombs - AMNs.size(), view);

                }
            }
        }
    }

    public boolean isSatisfactory(VecInt clause) {
        ISolver solver = SolverFactory.newDefault();
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);

        try {
            solver.addAllClauses(formulas);
            solver.addClause(clause);
            IProblem problem = solver;
            return problem.isSatisfiable();
        } catch(ContradictionException e) {
            return false;
        } catch (TimeoutException e) {
            return false;
        }

    }

    private void addExact(ArrayList<int[]> AFNs, int bombs, char[][] view) {
        addMost(AFNs, bombs, view);
        addLeast(AFNs, bombs, view);
    }

    private void addMost(ArrayList<int[]> AFNs, int bombs, char[][] view) {
        if(bombs == AFNs.size()) {
            return;
        }else if (bombs == 0) {
            for(int i = 0; i < AFNs.size(); i++) {
                int el = flatten(AFNs.get(i)[0], AFNs.get(i)[1], view);
                variables.add(el);
                int[] clause = new int[] {-el};
                formulas.push(new VecInt(clause));
            }
            return;

        }

        List<List<Boolean>> cases = new ArrayList<>();
        List<Boolean> situation = new ArrayList<>();
        int selected = bombs + 1;
        getCases(cases, situation, 0, selected, AFNs.size());
        for(List<Boolean> sit : cases) {
            ArrayList<Integer> clauseList = new ArrayList<>();
            for(int i = 0; i < sit.size(); i++) {
                if(sit.get(i)) {
                    int el = flatten(AFNs.get(i)[0], AFNs.get(i)[1], view);
                    variables.add(el);
                    clauseList.add(-el);
                }
            }
            int[] clause = clauseList.stream().mapToInt(j -> j).toArray();
            formulas.push(new VecInt(clause));
        }
    }

    private void addLeast(ArrayList<int[]> AFNs, int bombs, char[][] view) {
        if(bombs == AFNs.size()) {
            for(int i = 0; i < AFNs.size(); i++) {
                int el = flatten(AFNs.get(i)[0], AFNs.get(i)[1], view);
                variables.add(el);
                int[] clause = new int[] {el};
                formulas.push(new VecInt(clause));
            }
            return;

        } else if (bombs == 0) {
            return;

        }

        List<List<Boolean>> cases = new ArrayList<>();
        List<Boolean> situation = new ArrayList<>();
        int selected = AFNs.size() - bombs + 1;
        getCases(cases, situation, 0, selected, AFNs.size());
        for(List<Boolean> sit : cases) {
            ArrayList<Integer> clauseList = new ArrayList<>();
            for(int i = 0; i < sit.size(); i++) {
                if(sit.get(i)) {
                    int el = flatten(AFNs.get(i)[0], AFNs.get(i)[1], view);
                    variables.add(el);
                    clauseList.add(el);
                }
            }
            int[] clause = clauseList.stream().mapToInt(j -> j).toArray();
            formulas.push(new VecInt(clause));
        }
    }


    public int flatten(int x, int y, char[][] view) {
        return y * view[0].length + x + 1;
    }

    private void init(char[][] view) {
        formulas = new Vec<>();
        variables = new TreeSet<>();
        MAXVAR = view.length * view[0].length + SAFE_VAR_NUMBER;
        NBCLAUSES = view.length * view[0].length * SAFE_CLAUSE_NUMBER;
    }

    public SortedSet<Integer> getVariables() {
        return variables;
    }
}
