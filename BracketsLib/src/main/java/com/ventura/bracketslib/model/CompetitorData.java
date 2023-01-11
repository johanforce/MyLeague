package com.ventura.bracketslib.model;

import java.io.Serializable;

/**
 * Created by Emil on 21/10/17.
 */

public class CompetitorData implements Serializable {
    private int icLogo;
    private String name;
    private String score;

    public CompetitorData(int icLogo, String name, String score) {
        this.name = name;
        this.score = score;
        this.icLogo = icLogo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcLogo() {
        return icLogo;
    }

    public void setIcLogo(int icLogo) {
        this.icLogo = icLogo;
    }
}
