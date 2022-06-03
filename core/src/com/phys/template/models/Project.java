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

    public void addExercise(int exerciseNumber) {
        if (containsExercise(exerciseNumber)) {
            logger.error("Already has the exercise " + exerciseNumber, new GdxRuntimeException(""));
        }

        Exercise exercise = PhysTemplate.Instance().ProjectController().getExerciseModelFor(exerciseNumber);
        exercises.add(exercise);
        exerciseNumbers.add(exerciseNumber);
        for (Person person : people) {
            if (person.availableExercises.contains(exerciseNumber)) {
                person.addExercise(exerciseNumber);
                PhysTemplate.Instance().DataController().calculatePersonPoints(person);
                PhysTemplate.Instance().DataController().calculatePersonGrade(person);
            }
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
        for (Person person : people) {
            person.ageGroup = PhysTemplate.Instance().DataController().getAgeGroupFor(person.ageGroupNumber);

            IntArray restrictionIndexes = person.restrictionIndexes;
            for (int i = 0; i< restrictionIndexes.size; i++) {
                person.restrictions.add(PhysTemplate.Instance().DataController().getRestrictionFor(restrictionIndexes.get(i)));
            }
            PhysTemplate.Instance().DataController().fetchPersonAvailableExercises(person, this);
        }

        for (int i = 0; i < exerciseNumbers.size; i++) {
            int exerciseNumber = exerciseNumbers.get(i);
            Exercise exercise = PhysTemplate.Instance().ProjectController().getExerciseModelFor(exerciseNumber);
            exercises.add(exercise);
            for (Person person : people) {
                if (person.availableExercises.contains(exerciseNumber)) {
                    person.addExercise(exerciseNumber);
                }
            }
        }

        for (Person person : people) {
            PhysTemplate.Instance().DataController().calculatePersonPoints(person);
            PhysTemplate.Instance().DataController().calculatePersonGrade(person);
        }

        calculateFinalGrade();
    }

    public void calculateFinalGrade() {
        boolean minusOne = false;
        float peopleCount = (float)getCheckedCount();
        int restrictedPeopleCount = getRestrictedPeopleCount();
        float restrictedPercent = restrictedPeopleCount / peopleCount * 100f;
        if (restrictedPercent >= 25) {
            minusOne = true;
        }

        int countForExc = getCountForGrade(Grade.EXCELLENT);
        float excPercent = countForExc / peopleCount * 100f;

        int countForGood = getCountForGrade(Grade.GOOD);
        float goodPercent = countForGood / peopleCount * 100f;

        int countForOk = getCountForGrade(Grade.OK);
        int countForBadGrade = getCountForGrade(Grade.BAD);

        float overallOkPercent = (peopleCount - countForBadGrade) / peopleCount * 100f;

        if (areStudents) {
            if (overallOkPercent >= 95 && excPercent >= 50) {
                finalGrade = minusOne ? Grade.GOOD : Grade.EXCELLENT;
                return;
            }

            if (overallOkPercent >= 90 && excPercent + goodPercent >= 50) {
                finalGrade = minusOne ? Grade.OK : Grade.GOOD;
                return;
            }

            if (overallOkPercent >= 85) {
                finalGrade = minusOne ? Grade.BAD : Grade.OK;
                return;
            }

            finalGrade = Grade.BAD;
        } else {
            if (overallOkPercent >= 90 && excPercent >= 50) {
                finalGrade = minusOne ? Grade.GOOD : Grade.EXCELLENT;
                return;
            }

            if (overallOkPercent >= 80 && excPercent + goodPercent >= 50) {
                finalGrade = minusOne ? Grade.OK : Grade.GOOD;
                return;
            }

            if (overallOkPercent >= 70) {
                finalGrade = minusOne? Grade.BAD : Grade.OK;
                return;
            }

            finalGrade = Grade.BAD;
        }
    }

    public int getCountForGrade(Grade grade) {
        int count = 0;
        for (Person person : people) {
            if (person.canCalculateFinalGrade) {
                if (person.getGrade().getNumericalGrade() == grade.getNumericalGrade()) {
                    count++;
                }
            }
        }

        return count;
    }

    public float getPercentForGrade(Grade grade) {
        int checkedCount = getCheckedCount();
        if (checkedCount == 0) return 0;
        float percent = (getCountForGrade(grade) / (float) checkedCount) * 100f;
        return (float) Math.round((percent * 100.0) / 100.0);
    }

    public Grade getFinalGrade() {
        return finalGrade;
    }

    public float getNormalPercent() {
        int countForGrade = getCheckedCount() - getCountForGrade(Grade.BAD);
        float overallOkPercent = countForGrade / ((float) getCheckedCount()) * 100f;
        return (float) Math.round((overallOkPercent * 100.0) / 100.0);
    }

    public void setStudents(boolean areStudents) {
        this.areStudents = areStudents;
        for (Person person : people) {
            updateAvailableExercises(person);
        }
    }

    public void setAviation(boolean isAviation) {
        this.isAviation = isAviation;
        for (Person person : people) {
            updateAvailableExercises(person);
        }
    }

    public void updateAvailableExercises(Person person) {
        PhysTemplate.Instance().DataController().fetchPersonAvailableExercises(person, this);
        PhysTemplate.Instance().DataController().calculatePersonPoints(person);
        PhysTemplate.Instance().DataController().calculatePersonGrade(person);
        calculateFinalGrade();
    }

    public void removeRestrictionFrom(Person person, int restrictionIndex) {
        if (!person.hasRestriction(restrictionIndex)) {
            logger.error("Person doesn't have the restriction " + restrictionIndex);
        }

        Restriction restriction = PhysTemplate.Instance().DataController().getRestrictionFor(restrictionIndex);
        person.restrictions.removeValue(restriction, false);
        person.restrictionIndexes.removeValue(restrictionIndex);
        PhysTemplate.Instance().UIStage().updateContent();
    }

    public void addRestrictionTo(Person person, int restrictionIndex) {
        if (person.hasRestriction(restrictionIndex)) {
            logger.error("Person already has the restriction " + restrictionIndex);
        }

        Restriction restriction = PhysTemplate.Instance().DataController().getRestrictionFor(restrictionIndex);
        person.restrictions.add(restriction);
        person.restrictionIndexes.add(restrictionIndex);
        PhysTemplate.Instance().UIStage().updateContent();
    }

    public int getCheckedCount() {
        int counter = 0;
        for (Person person : people) {
            if (person.canCalculateFinalGrade) {
                counter++;
            }
        }

        return counter;
    }

    public int getRestrictedPeopleCount() {
        int counter = 0;
        for (Person person : people) {
            PersonGradeCalculationError gradeCalculationError = person.gradeCalculationError;
            if (gradeCalculationError != null && gradeCalculationError.type.equals(CalculationErrorType.TOO_MANY_RESTRICTIONS)) {
                counter++;
            }
        }

        return counter;
    }
}
