package util;

import java.util.ArrayList;
import java.util.List;

public interface Combination {
     default void getCases(List<List<Boolean>> cases, List<Boolean> situation, int index, int selected, int sizeOfAFN) {
        if(selected == 0 && index == sizeOfAFN) {
            cases.add(new ArrayList<>(situation));
            return;
        }
        if (index >= sizeOfAFN) {
            return;
        }
        if(selected > 0) {
            situation.add(true);
            getCases(cases, situation, index + 1, selected - 1, sizeOfAFN);
            situation.remove(situation.size()-1);
        }
        situation.add(false);
        getCases(cases, situation, index + 1, selected, sizeOfAFN);
        situation.remove(situation.size()-1);
    }
}
