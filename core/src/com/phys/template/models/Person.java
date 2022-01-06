package com.phys.template.models;

import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.phys.template.PhysTemplate;

import java.util.ArrayList;

public class Person {
    private static final Logger logger = new Logger("PersonClass");

    public Rank rank;
    public String name;
    public String surname;
    public String fatherName;
    public Sex sex;
    public int ageGroupNumber;
    public Category category;
    public int index;

    public transient AgeGroup ageGroup;
    private transient int overallPoints;
    private transient int finalGrade;
    public transient boolean canCalculateFinalGrade = false;
    public transient PersonGradeCalculationError gradeCalculationError;

    private transient final OrderedMap<Integer, Integer> exercisesPoints;
    private final OrderedMap<Integer, Float> floatExercisesRaw;
    private final OrderedMap<Integer, Integer> intExercisesRaw;
    public transient ArrayList<Integer> attachedExercises;

    public ArrayList<String> notes;

    public Person() {
        attachedExercises = new ArrayList<>();
        exercisesPoints = new OrderedMap<>();
        floatExercisesRaw = new OrderedMap<>();
        intExercisesRaw = new OrderedMap<>();
        notes = new ArrayList<>();
    }

    public String getFullName() {
        return surname + " " + name + " " + fatherName;
    }

    public void addExercise(int number) {
        attachedExercises.add(number);
    }

    public int getExercisePoint(int number) {
        if (!exercisesPoints.containsKey(number)) {
            logger.debug(name + " person doesn't contain exercise " + number, new RuntimeException());
            return 0;
        }

        return exercisesPoints.get(number);
    }

    public boolean hasFilledRawValue(int number) {
        if (PhysTemplate.Instance().DataController().isFloatExercise(number)) {
            return floatExercisesRaw.containsKey(number);
        } else {
            return intExercisesRaw.containsKey(number);
        }
    }

    public int getOverallPoints() {
        return overallPoints;
    }

    public int getIntExerciseRawValue (int number) {
        return intExercisesRaw.get(number);
    }

    public float getFloatExerciseRawValue(int number) {
        return floatExercisesRaw.get(number);
    }

    public void putFloatExerciseRawValue(int number, float value) {
        floatExercisesRaw.put(number, value);
    }

    public void putIntExerciseRawValue(int number, int value) {
        intExercisesRaw.put(number, value);
    }

    public Person copy() {
        Person copy = new Person();
        copy.name = this.name;
        copy.rank = this.rank;
        copy.ageGroup = this.ageGroup;
        copy.ageGroupNumber = this.ageGroupNumber;
        copy.surname = this.surname;
        copy.fatherName = this.fatherName;
        copy.sex = this.sex;
        copy.notes.addAll(this.notes);
        copy.category = this.category;
        copy.overallPoints = this.overallPoints;
        copy.finalGrade = this.finalGrade;
        copy.index = this.index;
        copy.canCalculateFinalGrade = this.canCalculateFinalGrade;
        copy.gradeCalculationError = this.gradeCalculationError;

        copy.attachedExercises.addAll(attachedExercises);
        for (ObjectMap.Entry<Integer, Float> integerFloatEntry : this.floatExercisesRaw) {
            copy.floatExercisesRaw.put(integerFloatEntry.key, integerFloatEntry.value);
        }
        for (ObjectMap.Entry<Integer, Integer> integerFloatEntry : this.intExercisesRaw) {
            copy.intExercisesRaw.put(integerFloatEntry.key, integerFloatEntry.value);
        }
        for (ObjectMap.Entry<Integer, Integer> exercisesPoint : exercisesPoints) {
            copy.exercisesPoints.put(exercisesPoint.key, exercisesPoint.value);
        }

        return copy;
    }

    public void removeExercise(int exerciseNumber) {
        // TODO: 11/23/2021 np exception here
        attachedExercises.remove(Integer.valueOf(exerciseNumber));
        exercisesPoints.remove(exerciseNumber);
        if (PhysTemplate.Instance().DataController().isFloatExercise(exerciseNumber)) {
            floatExercisesRaw.remove(exerciseNumber);
        } else {
            intExercisesRaw.remove(exerciseNumber);
        }
    }

    public void copyFrom(Person copyPerson) {
        this.name = copyPerson.name;
        this.rank = copyPerson.rank;
        this.ageGroup = copyPerson.ageGroup;
        this.ageGroupNumber = copyPerson.ageGroupNumber;
        this.surname = copyPerson.surname;
        this.fatherName = copyPerson.fatherName;
        this.sex = copyPerson.sex;
        notes.clear();
        notes.addAll(copyPerson.notes);
        this.category = copyPerson.category;
        this.overallPoints = copyPerson.overallPoints;
        this.finalGrade = copyPerson.finalGrade;
        this.index = copyPerson.index;
        this.gradeCalculationError = copyPerson.gradeCalculationError;
        this.canCalculateFinalGrade = copyPerson.canCalculateFinalGrade;

        attachedExercises.clear();
        attachedExercises.addAll(copyPerson.attachedExercises);
        floatExercisesRaw.clear();
        for (ObjectMap.Entry<Integer, Float> integerFloatEntry : copyPerson.floatExercisesRaw) {
            floatExercisesRaw.put(integerFloatEntry.key, integerFloatEntry.value);
        }
        intExercisesRaw.clear();
        for (ObjectMap.Entry<Integer, Integer> integerFloatEntry : copyPerson.intExercisesRaw) {
            intExercisesRaw.put(integerFloatEntry.key, integerFloatEntry.value);
        }

        exercisesPoints.clear();
        for (ObjectMap.Entry<Integer, Integer> exercisesPoint : copyPerson.exercisesPoints) {
            exercisesPoints.put(exercisesPoint.key, exercisesPoint.value);
        }

    }

    public void setGrade(Integer grade) {
        this.finalGrade = grade;
    }

    public void setOverallPoints(int overallPoints) {
        this.overallPoints = overallPoints;
    }

    public void putExercisePoint(Integer attachedExercise, int gradePoint) {
        exercisesPoints.put(attachedExercise, gradePoint);
    }

    public int getGrade() {
        return finalGrade;
    }
}
