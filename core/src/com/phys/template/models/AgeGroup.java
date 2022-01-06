package com.phys.template.models;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Objects;

public class AgeGroup {
    public int number;
    public String description;


    public AgeGroup (int number) {
        this.number = number;
        description = "";
    }

    public String getShortDescription() {
        return description;
    }

    public AgeGroup copy() {
        AgeGroup ageGroup = new AgeGroup(this.number);
        ageGroup.description = this.description;
        return ageGroup;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeGroup ageGroup = (AgeGroup) o;
        return number == ageGroup.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, description);
    }
}
