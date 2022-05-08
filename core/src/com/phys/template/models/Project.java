package com.phys.template.models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import com.phys.template.PhysTemplate;
import com.phys.template.views.metaWidgets.MetaInfoGroupWidget;

import java.util.ArrayList;

public class Project {

    private transient final ArrayList<Exercise> exercises;

    private final  IntArray exerciseNumbers;
    private final ArrayList<Person> people;
    private final Metadata metadata;
    public boolean areStudents = false;
    public boolean isAviation = false;

    private transient Grade finalGrade = Grade.BAD;
    private transient String fileName;

    private static  final Logger logger = new Logger("Project logger");

    public Project() {
        exercises = new ArrayList<>();
        exerciseNumbers = new IntArray();
        people = new ArrayList<>();
        metadata = new Metadata();
    }

    public void importMetaDatas() {
        MetaInfoGroupWidget metaWidget = PhysTemplate.Instance().UIStage().getMetaWidget();
        metadata.setSquadName(metaWidget.getSquadName());
        String baseNumber = metaWidget.getBaseNumber();
        if (baseNumber.matches("[0-9]+")) {
            metadata.setBaseNumber(baseNumber);
        } else {
            metadata.setBaseNumber("");
        }
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
        calculateFinalGrade();
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

    public Metadata getMetadata() {
        return metadata;
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
        calculateFinalGrade();
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
            calculateFinalGrade();
        }
    }

    public void calculateFinalGrade() {
        int overallPoints = 5 * getCountForGrade(Grade.EXCELLENT) +
                4 * getCountForGrade(Grade.GOOD) +
                3 * getCountForGrade(Grade.OK) +
                2 * getCountForGrade(Grade.BAD);
        int gradeValue = Math.round(overallPoints / ((float) getPeopleCount()));
        finalGrade = Grade.gradeTypeForGrade(gradeValue);
        if (finalGrade == null) {
            finalGrade = Grade.BAD;
        }

    }

    public int getCountForGrade(Grade grade) {
        int count = 0;
        for (Person person : people) {
            if (person.getGrade().getNumericalGrade() == grade.getNumericalGrade()) {
                count++;
            }
        }

        return count;
    }

    public float getPercentForGrade(Grade grade) {
        float percent = (getCountForGrade(grade) / (float) getPeopleCount()) * 100f;
        return (float) Math.round((percent * 100.0) / 100.0);
    }

    public Grade getFinalGrade() {
        return finalGrade;
    }

    public float getNormalPercent() {
        int countForGrade = getPeopleCount() - getCountForGrade(Grade.BAD);
        float overallOkPercent = countForGrade / ((float) getPeopleCount()) * 100f;
        return (float) Math.round((overallOkPercent * 100.0) / 100.0);
    }

    public void setStudents(boolean areStudents) {
        this.areStudents = areStudents;
        for (Person person : people) {
            updateAvailableExercises(person);
        }

        calculateFinalGrade();
    }

    public void setAviation(boolean isAviation) {
        this.isAviation = isAviation;
        for (Person person : people) {
            updateAvailableExercises(person);
        }
    }

    public void updateAvailableExercises(Person person) {
        person.availableExercises.clear();
        PhysTemplate.Instance().DataController().fetchPersonAvailableExercises(person, this);
    }
}
