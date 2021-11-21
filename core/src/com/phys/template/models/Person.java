package com.phys.template.models;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;

public class Person {
    private static Logger logger = new Logger("PersonClass");

    public String rank;
    public String name;
    public String surname;
    public String fatherName;
    public boolean isWoman;
    public int age;
    private int ageGroup;
    public int category;
    private int overallPoints;
    private int finalGrade;

    public ArrayList<String> notes;

    private IntMap<Float> exerciseMap;

    public Person() {
        exerciseMap = new IntMap<>();
        notes = new ArrayList<>();
    }

    public void addExercise(int number) {
        exerciseMap.put(number, -1f);
    }

    public float getExerciseValue (int number) {
        if (exerciseMap.containsKey(number)) {
            logger.error(name + " person doesn't contain exercise " + number, new RuntimeException());
        }

        return  exerciseMap.get(number);
    }

    public void putExerciseValue (int number, float value) {
        // TODO: 11/19/2021 solve issue with -1
        exerciseMap.put(number, value);
    }

    public Person copy() {
        Person copy = new Person();
        copy.name = this.name;
        copy.rank = this.rank;
        copy.age = this.age;
        copy.ageGroup = this.ageGroup;
        copy.surname = this.surname;
        copy.fatherName = this.fatherName;
        copy.isWoman = this.isWoman;
        copy.notes.addAll(this.notes);
        copy.category = this.category;
        copy.overallPoints = this.overallPoints;
        copy.finalGrade = this.finalGrade;
        for (IntMap.Entry<Float> entry : this.exerciseMap) {
            copy.exerciseMap.put(entry.key, entry.value);
        }
        return copy;
    }
}
