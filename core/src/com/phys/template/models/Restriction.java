package com.phys.template.models;

import com.badlogic.gdx.utils.IntArray;

import java.util.Objects;

public class Restriction {
    private final int index;
    private final String name;
    private final IntArray restrictsSoldiersFrom;
    private final IntArray restrictsOfficersFrom;

    public Restriction (int index, String name, IntArray restrictsSoldiersFrom, IntArray restrictsOfficersFrom) {
        this.index = index;
        this.name = name;
        this.restrictsSoldiersFrom = restrictsSoldiersFrom;
        this.restrictsOfficersFrom = restrictsOfficersFrom;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public boolean doesRestrictFrom (int exerciseNumber, boolean isSoldier) {
        return isSoldier ? restrictsSoldiersFrom.contains(exerciseNumber) : restrictsOfficersFrom.contains(exerciseNumber);
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
        return new Restriction(index, name, restrictsSoldiersFrom, restrictsOfficersFrom);
    }
}
