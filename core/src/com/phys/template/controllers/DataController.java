package com.phys.template.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.phys.template.models.AgeGroup;
import com.phys.template.models.Category;
import com.phys.template.models.Exercise;
import com.phys.template.models.Person;

import java.util.ArrayList;

public class DataController {
    private final JsonReader json = new JsonReader();


    // AgeGroup -> Category -> ExerciseCount -> Grade -> Point
    private final ObjectMap<AgeGroup, ObjectMap<Category, IntMap<IntMap<Integer>>>> pointMap = new ObjectMap<>();

    public void fillPointData () {
        
    }

    public ArrayList<Exercise> loadExercisesData() {
        ArrayList<Exercise> exercises = new ArrayList<>();

        JsonValue base = json.parse(Gdx.files.internal("exercises.json"));

        for (JsonValue jsonValue : base) {
            Exercise exercise = new Exercise();
            exercise.number = jsonValue.getInt("number");
            exercise.longName = jsonValue.getString("longName");
            exercise.name = jsonValue.getString("name");

            exercises.add(exercise);
        }

        return exercises;
    }

    public void calculatePersonPoints(Person person) {
        // TODO: 11/22/2021 calculatePoints
    }
}
