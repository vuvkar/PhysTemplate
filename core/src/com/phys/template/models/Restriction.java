package com.phys.template.models;

import com.badlogic.gdx.utils.IntArray;

public class Restriction {
    private final String name;
    private final boolean isForSoldier;
    private final IntArray restrictsFrom;

    public Restriction (String name, boolean isForSoldier, IntArray restrictsFrom) {
        this.name = name;
        this.isForSoldier = isForSoldier;
        this.restrictsFrom = restrictsFrom;
    }

    public String getName() {
        return name;
    }

    public boolean isForSoldier() {
        return isForSoldier;
    }

    public boolean doesRestrictFrom (int exerciseNumber) {
        return restrictsFrom.contains(exerciseNumber);
    }
}
