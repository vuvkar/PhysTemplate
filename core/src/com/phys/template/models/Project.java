package com.phys.template.models;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.phys.template.PhysTemplate;

import java.util.ArrayList;

public class Project {
    private ArrayList<Exercise> exercises;
    private ArrayList<Person> people;

    private static Logger logger = new Logger("Project logger");

    public Project() {
        exercises = new ArrayList<>();
        people = new ArrayList<>();
    }

    public void addExercise(int exerciseNumber) {
        if (containsExercise(exerciseNumber)) {
            logger.error("Already has the exercise " + exerciseNumber, new GdxRuntimeException(""));
        }

        Exercise exercise = PhysTemplate.Instance().ProjectController().getExerciseModelFor(exerciseNumber);
        exercises.add(exercise);
        for (Person person : people) {
            person.addExercise(exerciseNumber);
        }
    }

    public boolean containsExercise(int exerciseNumber) {
        for (Exercise exercise : exercises) {
            if (exercise.number == exerciseNumber) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Exercise> getExercises() {
        ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
        for (Exercise exercise : exercises) {
            exerciseArrayList.add(exercise.copy());
        }

        return exerciseArrayList;
    }

    public ArrayList<Person> getPeople() {
        ArrayList<Person> peopleList = new ArrayList<>();
        for (Person person : people) {
            peopleList.add(person.copy());
        }
        return peopleList;
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void removeExercise(int exerciseNumber) {
        boolean didRemove = false;
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            if (exercise.number == exerciseNumber) {
                exercises.remove(exercise);
                didRemove = true;
            }
        }

        if(!didRemove) {
            logger.error("Exercise not found to be removed " + exerciseNumber, new GdxRuntimeException(""));
        }

        for (Person person : people) {
            person.removeExercise(exerciseNumber);
        }
    }

    public int getPeopleCount() {
        return people.size();
    }

    public void updatePersonData(Person copyPerson) {
        for (Person original : people) {
            if (original.index == copyPerson.index) {
                original.copyFrom(copyPerson);
            }
        }
    }
}
