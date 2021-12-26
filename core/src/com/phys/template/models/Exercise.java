package com.phys.template.models;

public class Exercise {

    public int number;
    public String name;
    public  String longName;
    public String description;
    public boolean isCustom;
    public boolean isFloat;
    public String unit;
    // Վարժության արդյունքի մեծացման հետ միավորները փոքրանում են թե ոչ
    public boolean arePointsDescending;

    public Exercise copy() {
        Exercise exercise = new Exercise();
        exercise.longName = this.longName;
        exercise.number = this.number;
        exercise.name = this.name;
        exercise.description = this.description;
        exercise.isCustom = this.isCustom;
        exercise.isFloat = this.isFloat;
        exercise.unit = this.unit;
        exercise.arePointsDescending = this.arePointsDescending;

        return exercise;
    }

    public String getUnit() {
        return unit;
    }
}


