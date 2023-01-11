package com.ventura.bracketslib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emil on 21/10/17.
 */

public class ColomnData implements Serializable {

    public ColomnData(String nameRound, List<MatchData> matches) {
        this.nameRound = nameRound;
        this.matches = matches;
    }

    private List<MatchData> matches;
    private String nameRound;

    public void setMatches(List<MatchData> matches) {
        this.matches = matches;
    }

    public String getNameRound() {
        return nameRound;
    }

    public void setNameRound(String nameRound) {
        this.nameRound = nameRound;
    }

    public void setMatches(ArrayList<MatchData> matches) {
        this.matches = matches;
    }

    public List<MatchData> getMatches() {
        return matches;
    }
}
