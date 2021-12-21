package com.phys.template.models;

import com.badlogic.gdx.utils.GdxRuntimeException;

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
        return description.substring(0, 15) + "...";
    }
}
