package com.phys.template.models;

import com.badlogic.gdx.utils.*;
import com.phys.template.PhysTemplate;

import java.util.*;

public class Person implements Json.Serializable {
    private static final Logger logger = new Logger("PersonClass");

    public Rank rank;
    public String name;
    public Sex sex;
    public int ageGroupNumber;
    public Category category;
    public int index;

    public transient AgeGroup ageGroup;
    private transient int overallPoints;
    private transient Grade finalGrade = Grade.BAD;
    public transient boolean canCalculateFinalGrade = false;
    public transient PersonGradeCalculationError gradeCalculationError;
    public transient IntArray availableExercises;

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
        availableExercises = new IntArray();
        notes = new ArrayList<>();
        ageGroup = PhysTemplate.Instance().DataController().getAgeGroupFor(1);
        ageGroupNumber = 1;
        category = Category.FIRST;
        rank = Rank.SHN;
        sex = Sex.MALE;
    }

    public String getFullName() {
        return name;
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
        if (!availableExercises.contains(number)) {
            logger.error("PUTTING UNAVAILABLE EXERCISE VALUE");
            return;
        }
        floatExercisesRaw.put(number, value);
    }

    public void putIntExerciseRawValue(int number, int value) {
        if (!availableExercises.contains(number)) {
            logger.error("PUTTING UNAVAILABLE EXERCISE VALUE");
            return;
        }
        intExercisesRaw.put(number, value);
    }

    public Person copy() {
        Person copy = new Person();
        copy.name = this.name;
        copy.rank = this.rank;
        copy.ageGroup = this.ageGroup;
        copy.ageGroupNumber = this.ageGroupNumber;
        copy.sex = this.sex;
        copy.notes.addAll(this.notes);
        copy.category = this.category;
        copy.overallPoints = this.overallPoints;
        copy.finalGrade = this.finalGrade;
        copy.index = this.index;
        copy.canCalculateFinalGrade = this.canCalculateFinalGrade;
        copy.gradeCalculationError = this.gradeCalculationError;

        copy.attachedExercises.addAll(attachedExercises);
        for (ObjectMap.Entry<Integer, Float> entry : floatExercisesRaw) {
            copy.floatExercisesRaw.put(entry.key, entry.value);
        }
        copy.availableExercises.addAll(availableExercises);

        for (ObjectMap.Entry<Integer, Integer> entry : intExercisesRaw) {
            copy.intExercisesRaw.put(entry.key, entry.value);
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

        availableExercises.clear();
        availableExercises.addAll(copyPerson.availableExercises);
        floatExercisesRaw.clear();
        for (ObjectMap.Entry<Integer, Float> integerFloatEntry : copyPerson.floatExercisesRaw) {
            floatExercisesRaw.put(integerFloatEntry.key, integerFloatEntry.value);
        }
        intExercisesRaw.clear();
        for (ObjectMap.Entry<Integer, Integer> integerIntegerEntry : copyPerson.intExercisesRaw) {
            intExercisesRaw.put(integerIntegerEntry.key, integerIntegerEntry.value);
        }

        exercisesPoints.clear();
        for (ObjectMap.Entry<Integer, Integer> exercisesPoint : copyPerson.exercisesPoints) {
            exercisesPoints.put(exercisesPoint.key, exercisesPoint.value);
        }

    }

    public void setGrade(Grade grade) {
        this.finalGrade = grade;
    }

    public void setOverallPoints(int overallPoints) {
        this.overallPoints = overallPoints;
    }

    public void putExercisePoint(Integer attachedExercise, int gradePoint) {
        exercisesPoints.put(attachedExercise, gradePoint);
    }

    public Grade getGrade() {
        return finalGrade;
    }

    @Override
    public void write(Json json) {
        json.writeValue("rank", rank.ordinal());
        json.writeValue("name", name);
        json.writeValue("sex", sex.ordinal());
        json.writeValue("ageGroupNumber", ageGroupNumber);
        json.writeValue("category", category.ordinal());
        json.writeValue("index", index);

        json.writeArrayStart("floatExercises");
        for (ObjectMap.Entry<Integer, Float> integerFloatEntry : floatExercisesRaw) {
            json.writeObjectStart();
            json.writeValue("exerciseNumber", integerFloatEntry.key);
            json.writeValue("exerciseRawValue", integerFloatEntry.value);
            json.writeObjectEnd();
        }
        json.writeArrayEnd();

        json.writeArrayStart("intExercises");
        for (ObjectMap.Entry<Integer, Integer> integerFloatEntry : intExercisesRaw) {
            json.writeObjectStart();
            json.writeValue("exerciseNumber", integerFloatEntry.key);
            json.writeValue("exerciseRawValue", integerFloatEntry.value);
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        rank = Rank.values()[jsonData.getInt("rank")];
        sex = Sex.values()[jsonData.getInt("sex")];
        category = Category.values()[jsonData.getInt("category")];
        name = jsonData.getString("name");
        ageGroupNumber = jsonData.getInt("ageGroupNumber");
        index = jsonData.getInt("index");
        floatExercisesRaw.clear();
        JsonValue floatExercises = jsonData.get("floatExercises");
        for (JsonValue floatExercise : floatExercises) {
            floatExercisesRaw.put(floatExercise.getInt("exerciseNumber"), floatExercise.getFloat("exerciseRawValue"));
        }

        intExercisesRaw.clear();
        JsonValue intExercises = jsonData.get("intExercises");
        for (JsonValue intExercise : intExercises) {
            intExercisesRaw.put(intExercise.getInt("exerciseNumber"), intExercise.getInt("exerciseRawValue"));
        }
    }
}
