package com.phys.template.models;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.ArrayList;

public class Person {
    private static Logger logger = new Logger("PersonClass");

    public Rank rank;
    public String name;
    public String surname;
    public String fatherName;
    public Sex sex;
    public AgeGroup ageGroup;
    public Category category;
    public int index;

    private int overallPoints;
    private int finalGrade;

    private OrderedMap<Integer, Integer> exercisesPoints;
    private OrderedMap<Integer, Float> exercisesRaw;
    public ArrayList<Integer> attachedExercises;

    public ArrayList<String> notes;

    public Person() {
        attachedExercises = new ArrayList<>();
        exercisesPoints = new OrderedMap<>();
        exercisesRaw = new OrderedMap<>();
        notes = new ArrayList<>();
    }

    public String getFullName() {
        return surname + " " + name + " " + fatherName;
    }

    public void addExercise(int number) {
        attachedExercises.add(number);
    }

    public float getExercisePoint (int number) {
        if (exercisesPoints.containsKey(number)) {
            logger.error(name + " person doesn't contain exercise " + number, new RuntimeException());
        }

        return  exercisesPoints.get(number);
    }

    public void putExerciseRawValue(int number, float value) {
        exercisesRaw.put(number, value);
    }

    public Person copy() {
        Person copy = new Person();
        copy.name = this.name;
        copy.rank = this.rank;
        copy.ageGroup = this.ageGroup;
        copy.surname = this.surname;
        copy.fatherName = this.fatherName;
        copy.sex = this.sex;
        copy.notes.addAll(this.notes);
        copy.category = this.category;
        copy.overallPoints = this.overallPoints;
        copy.finalGrade = this.finalGrade;
        copy.index = this.index;

        copy.attachedExercises.addAll(attachedExercises);
        for (ObjectMap.Entry<Integer, Float> integerFloatEntry : this.exercisesRaw) {
            copy.exercisesRaw.put(integerFloatEntry.key, integerFloatEntry.value);
        }
        for (ObjectMap.Entry<Integer, Integer> exercisesPoint : exercisesPoints) {
            copy.exercisesPoints.put(exercisesPoint.key, exercisesPoint.value);
        }

        return copy;
    }

    public void removeExercise(int exerciseNumber) {
        for (Integer attachedExercise : attachedExercises) {
            if (attachedExercise == exerciseNumber) {
                attachedExercises.remove(attachedExercise);
            }
        }
        exercisesPoints.remove(exerciseNumber);
        exercisesRaw.remove(exerciseNumber);
    }
}
