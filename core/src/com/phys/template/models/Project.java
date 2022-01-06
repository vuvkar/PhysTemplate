package com.phys.template.models;

import com.badlogic.gdx.utils.*;
import com.phys.template.PhysTemplate;

import java.util.ArrayList;

public class Project {

    private transient final ArrayList<Exercise> exercises;

    private final  IntArray exerciseNumbers;
    private final ArrayList<Person> people;
    private final Metadata metadata;

    private transient String fileName;

    private static  final Logger logger = new Logger("Project logger");

    public Project() {
        exercises = new ArrayList<>();
        exerciseNumbers = new IntArray();
        people = new ArrayList<>();
        metadata = new Metadata();
    }

    public void addExercise(int exerciseNumber) {
        if (containsExercise(exerciseNumber)) {
            logger.error("Already has the exercise " + exerciseNumber, new GdxRuntimeException(""));
        }

        Exercise exercise = PhysTemplate.Instance().ProjectController().getExerciseModelFor(exerciseNumber);
        exercises.add(exercise);
        exerciseNumbers.add(exerciseNumber);
        for (Person person : people) {
            person.addExercise(exerciseNumber);
            PhysTemplate.Instance().DataController().calculatePersonPoints(person);
            PhysTemplate.Instance().DataController().calculatePersonGrade(person);
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

    public Array<Exercise> getExercises() {
        Array<Exercise> exerciseArrayList = new Array<>();
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

    public Person getPersonWithIndex(int index) {
        for (Person person : people) {
            if (person.index == index) {
                return person;
            }
        }

        return null;
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
        exerciseNumbers.removeValue(exerciseNumber);

        if(!didRemove) {
            logger.error("Exercise not found to be removed " + exerciseNumber, new GdxRuntimeException(""));
        }

        for (Person person : people) {
            person.removeExercise(exerciseNumber);
            PhysTemplate.Instance().DataController().calculatePersonPoints(person);
            PhysTemplate.Instance().DataController().calculatePersonGrade(person);
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

    public void deletePerson(int updatePersonIndex) {
        people.remove(updatePersonIndex);
        updatePeopleIndexes();
    }

    private void updatePeopleIndexes() {
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            person.index = i;
        }
    }

    public String getProjectString() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String data = json.prettyPrint(this);

        return data;
    }

    public void buildAfterLoad() {
        for (int i = 0; i < exerciseNumbers.size; i++) {
            int exerciseNumber = exerciseNumbers.get(i);
            Exercise exercise = PhysTemplate.Instance().ProjectController().getExerciseModelFor(exerciseNumber);
            exercises.add(exercise);
            for (Person person : people) {
                person.addExercise(exerciseNumber);
                person.ageGroup = PhysTemplate.Instance().DataController().getAgeGroupFor(person.ageGroupNumber);
                PhysTemplate.Instance().DataController().calculatePersonPoints(person);
                PhysTemplate.Instance().DataController().calculatePersonGrade(person);
            }
        }


    }
}
