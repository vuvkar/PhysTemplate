package com.phys.template.controllers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Logger;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Exercise;
import com.phys.template.models.Person;
import com.phys.template.models.Project;

import java.util.ArrayList;
import java.util.Comparator;

public class ProjectController {

    private static Logger logger = new Logger("ProjectController");

    private Project currentProject;

    public void init() {
    }

    public void newProject() {
        // TODO: 11/19/2021 handle new project creation
        Project project = new Project();
        currentProject = project;
    }

    public void loadProject(FileHandle handle) {
        // TODO: 11/19/2021 handle project file loading
    }

    public void updateRecentsList() {
        // TODO: 11/19/2021 handle recent file menu updating
    }

    public Exercise getExerciseModelFor(int exerciseNumber) {
        Exercise exercise = PhysTemplate.Instance().DataController().getExerciseModelFor(exerciseNumber);
        return  exercise.copy();
    }

    public Array<Exercise> getAvailableExercises() {
        Array<Exercise> exercises = new Array<>();
        for (Exercise value : PhysTemplate.Instance().DataController().getAllExercises()) {
            if (!currentProject.containsExercise(value.number)) {
                exercises.add(value.copy());
            }
        }
        exercises.sort(new Comparator<Exercise>() {
            @Override
            public int compare(Exercise o1, Exercise o2) {
                return o1.number - o2.number;
            }
        });

        return exercises;
    }

    public void addExerciseToCurrentProject(int exerciseNumber) {
        if (currentProject == null) {
            logger.error("Current project is null", new GdxRuntimeException(""));
        }
        currentProject.addExercise(exerciseNumber);
    }

    public ArrayList<Exercise> getCurrentProjectExercises() {
        if (currentProject == null) {
            logger.error("Current project is null", new GdxRuntimeException(""));
        }

        return currentProject.getExercises();
    }

    public ArrayList<Person> getCurrentProjectPeople() {
        if (currentProject == null) {
            logger.error("Current project is null", new GdxRuntimeException(""));
        }

        return currentProject.getPeople();
    }

    public void addPersonToCurrentProject(Person person) {
        if (currentProject == null) {
            logger.error("Current project is null", new GdxRuntimeException(""));
        }

        person.index = currentProject.getPeopleCount();
        PhysTemplate.Instance().DataController().calculatePersonPoints(person);
        currentProject.addPerson(person);
    }

    public void removeExercise(int exerciseNumber) {
        if (currentProject == null) {
            logger.error("Current project is null", new GdxRuntimeException(""));
        }
        currentProject.removeExercise(exerciseNumber);
        // TODO: 11/22/2021 refresh points
    }

    public int getPeopleCount() {
        return currentProject.getPeopleCount();
    }

    public void updatePersonData(Person person) {
        if (currentProject == null) {
            logger.error("Current project is null", new GdxRuntimeException(""));
        }
        currentProject.updatePersonData(person);
    }
}
