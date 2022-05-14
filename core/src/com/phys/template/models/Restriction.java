package com.phys.template.models;

import com.badlogic.gdx.utils.IntArray;

import java.util.Objects;

public class Restriction {
    private final int index;
    private final String name;
    private final boolean isForSoldier;
    private final IntArray restrictsFrom;

    public Restriction (int index, String name, boolean isForSoldier, IntArray restrictsFrom) {
        this.index = index;
        this.name = name;
        this.isForSoldier = isForSoldier;
        this.restrictsFrom = restrictsFrom;
    }

    public int getIndex() {
        return index;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restriction that = (Restriction) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public Restriction copy() {
        Restriction restriction = new Restriction(index, name, isForSoldier, restrictsFrom);

        return restriction;
    }
}
