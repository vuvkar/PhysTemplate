package com.phys.template.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import com.phys.template.PhysTemplate;
import com.phys.template.models.*;

public class DataController {

    private static final Logger logger = new Logger("DataController");

    private final JsonReader json = new JsonReader();

    // AgeGroup -> Category -> ExerciseCount -> Point -> Grade
    private final ObjectMap<AgeGroup, ObjectMap<Category, IntMap<IntMap<Integer>>>> gradeMap = new ObjectMap<>();

    private final IntMap<Exercise> loadedExercises = new IntMap<>();
    private final Array<Restriction> loadedSoldierRestrictions = new Array<>();
    private final Array<Restriction> loadedOfficerRestrictions = new Array<>();

    private final IntMap<ObjectMap<Float, Integer>> pointMap = new IntMap<>();
    private final IntMap<Array<Float>> sortedExercisePointKey = new IntMap<>();

    private final Array<AgeGroup> loadedAgeGroups = new Array<>();

    public DataController() {
        logger.setLevel(Logger.DEBUG);
        loadExercisesData();
        loadExercisePointData();
        loadRestrictionsData();
        loadGradePointData();
    }

    private void loadExercisesData() {
        JsonValue base = json.parse(Gdx.files.internal("exercises.json"));

        for (JsonValue jsonValue : base) {
            boolean isCustom = jsonValue.getBoolean("isCustom", false);
            if (isCustom) {
                continue;
            }
            Exercise exercise = new Exercise();
            exercise.number = jsonValue.getInt("number");
            exercise.longName = jsonValue.getString("longName");
            exercise.name = jsonValue.getString("name");
            exercise.unit = jsonValue.getString("unit");
            exercise.arePointsDescending = jsonValue.getBoolean("arePointsDescending", false);
            exercise.isFloat = jsonValue.getBoolean("isFloat");
            exercise.isCustom = isCustom;

            loadedExercises.put(exercise.number, exercise);
        }
    }

    private void loadRestrictionsData() {
        JsonValue base = json.parse(Gdx.files.internal("restrictions.json"));

        for (JsonValue jsonValue : base.get("soldier")) {
            int index = jsonValue.getInt("index");
            String name = jsonValue.getString("name");
            IntArray restrictsFrom = new IntArray();
            int[] restrictsFromJson = jsonValue.get("restricts").asIntArray();
            for (int i : restrictsFromJson) {
                restrictsFrom.add(i);
            }
            Restriction restriction = new Restriction(index, name, true, restrictsFrom);
            loadedSoldierRestrictions.add(restriction);
        }

        for (JsonValue jsonValue : base.get("officer")) {
            int index = jsonValue.getInt("index");
            String name = jsonValue.getString("name");
            IntArray restrictsFrom = new IntArray();
            int[] restrictsFromJson = jsonValue.get("restricts").asIntArray();
            for (int i : restrictsFromJson) {
                restrictsFrom.add(i);
            }
            Restriction restriction = new Restriction(index, name, false, restrictsFrom);
            loadedOfficerRestrictions.add(restriction);
        }
    }

    public void loadExercisePointData() {
        JsonValue base = json.parse(Gdx.files.internal("grades.json"));

        for (JsonValue ageGroupJson : base) {
            int ageGroupNumber = ageGroupJson.getInt("ageGroup");
            ObjectMap<Category, IntMap<IntMap<Integer>>> ageGroupObjectMap = new ObjectMap<>();
            AgeGroup ageGroup = new AgeGroup(ageGroupNumber);
            ageGroup.description = ageGroupJson.getString("description");
            gradeMap.put(ageGroup, ageGroupObjectMap);
            JsonValue categoryJson = ageGroupJson.get("category");
            loadedAgeGroups.add(ageGroup);
            for (JsonValue pointAbsJson : categoryJson) {
                JsonValue gradePointsJson = pointAbsJson.get("points");

                IntMap<IntMap<Integer>> categoryObjectMap = new IntMap<>();
                for (JsonValue exerciseCountJson : gradePointsJson) {
                    IntMap<Integer> gradeMap = new IntMap<>();
                    int exerciseCount = exerciseCountJson.getInt("exerciseCount");
                    JsonValue gradeValuesJson = exerciseCountJson.get("values");
                    for (JsonValue gradeValueJson : gradeValuesJson) {
                        int grade = gradeValueJson.getInt("grade");
                        int value = gradeValueJson.getInt("point");

                        gradeMap.put(value, grade);
                    }

                    categoryObjectMap.put(exerciseCount, gradeMap);
                }

                Category[] categories = Category.values();
                int[] ords = pointAbsJson.get("ord").asIntArray();
                for (int ord : ords) {
                    ageGroupObjectMap.put(categories[ord - 1], categoryObjectMap);
                }
            }

        }
    }

    public void loadGradePointData() {
        JsonValue base = json.parse(Gdx.files.internal("points.json"));

        for (JsonValue exercise : base) {
            int exerciseNumber = exercise.getInt("number");
            JsonValue points = exercise.get("points");
            ObjectMap<Float, Integer> floatMap = new ObjectMap<>();
            pointMap.put(exerciseNumber, floatMap);
            for (JsonValue point : points) {
                JsonValue child = point.child();
                float rawPoint = Float.parseFloat(child.name);
                int gradePoint = point.getInt(child.name);
                floatMap.put(rawPoint, gradePoint);
            }
            //add sorted array for fast searching
            Array<Float> floatArray = floatMap.keys().toArray();
            floatArray.sort();
            sortedExercisePointKey.put(exerciseNumber, floatArray);
        }
    }

    public void fetchPersonAvailableExercises(Person person, Project project) {
        IntArray availableExercises = person.availableExercises;
        availableExercises.clear();

        int ageGroupIndex = person.ageGroup.number;
        switch (ageGroupIndex) {
            // մալադոներ
            case 1:
                availableExercises.add(1, 4, 7, 8);
                availableExercises.add(9, 12);
                availableExercises.add(17, 18, 19, 21);
                availableExercises.add(25, 26);
                availableExercises.add(27, 28);
                break;
            // պարտադիր ժամկետային
            case 2:
                if (!project.isAviation) {
                    switch (person.category) {
                        case FIRST:
                            availableExercises.add(1, 4, 7, 8);
                            availableExercises.add(9, 14, 15, 16);
                            availableExercises.add(17, 18, 20, 21);
                            availableExercises.add(22, 23, 25, 26);
                            availableExercises.add(27, 24, 29);
                            break;
                        case SECOND:
                            availableExercises.add(1, 4, 7, 8);
                            availableExercises.add(9, 13, 15, 16);
                            availableExercises.add(17, 18, 20, 21);
                            availableExercises.add(22, 24, 25, 26);
                            availableExercises.add(27, 29);
                            break;
                        case THIRD:
                            availableExercises.add(1, 4, 7, 8);
                            availableExercises.add(9, 12, 15, 16);
                            availableExercises.add(17, 19, 21, 25);
                            availableExercises.add(26, 27, 29);
                            break;
                    }
                }
                break;
            // կուրսանտներ
            case 3:
            case 4:
            case 5:
                if (project.isAviation) {
                    for (int i = 1; i <= 11; i++) {
                        availableExercises.add(i);
                    }
                    for (int i = 24; i <= 27; i++) {
                        availableExercises.add(i);
                    }
                    availableExercises.add(28);
                } else {
                    switch (person.category) {
                        case FIRST:
                            for (int i = 1; i <= 9; i++) {
                                availableExercises.add(i);
                            }
                            for (int i = 14; i <= 18; i++) {
                                availableExercises.add(i);
                            }
                            for (int i = 21; i <= 27; i++) {
                                availableExercises.add(i);
                            }
                            availableExercises.add(29);
                            break;
                        case SECOND:
                            for (int i = 1; i <= 9; i++) {
                                availableExercises.add(i);
                            }
                            availableExercises.add(13, 15, 16, 17);
                            availableExercises.add(18, 29);
                            for (int i = 21; i <= 27; i++) {
                                availableExercises.add(i);
                            }
                            break;
                        case THIRD:
                            for (int i = 1; i <= 9; i++) {
                                availableExercises.add(i);
                            }
                            availableExercises.add(12, 15, 16, 17);
                            availableExercises.add(19, 21, 24, 25);
                            availableExercises.add(26, 27, 29);
                            break;
                    }
                }
                break;
            // 1-3 տարիքային խումբ
            case 6:
            case 7:
            case 8:
                if (project.isAviation) {
                    for (int i = 1; i <= 11; i++) {
                        availableExercises.add(i);
                    }
                    for (int i = 24; i <= 27; i++) {
                        availableExercises.add(i);
                    }
                    availableExercises.add(29);
                } else {
                    switch (person.category) {
                        case FIRST:
                            for (int i = 1; i <= 9; i++) {
                                availableExercises.add(i);
                            }
                            availableExercises.add(14, 15, 16, 17);
                            availableExercises.add(18, 20, 21, 22);
                            availableExercises.add(23);
                            availableExercises.add(25, 26, 27, 29);
                            // pahestazor as well
                            availableExercises.add(24);
                            break;
                        case SECOND:
                            for (int i = 1; i <= 9; i++) {
                                availableExercises.add(i);
                            }
                            availableExercises.add(13, 15, 16, 17);
                            availableExercises.add(18, 20, 21, 22);
                            availableExercises.add(25, 26, 27, 29);
                            // pahestazor as well
                            availableExercises.add(24, 30);
                            break;
                        case THIRD:
                            for (int i = 1; i <= 9; i++) {
                                availableExercises.add(i);
                            }
                            availableExercises.add(12, 15, 16, 17);
                            availableExercises.add(19, 21, 25, 26);
                            availableExercises.add(27, 29);
                            break;
                    }
                }
                break;
            // 4-5 տարիքային խումբ
            case 9:
            case 10:
                for (int i = 1; i <= 7; i++) {
                    availableExercises.add(i);
                }
                for (int i = 21; i <= 28; i++) {
                    availableExercises.add(i);
                }
                availableExercises.add(30);
                break;
            // 6-7 տարիքային խումբ
            case 11:
            case 12:
                availableExercises.add(1, 6, 21, 22);
                availableExercises.add(23, 28, 30);
                break;
            // կին զինծառայողներ 3-րդ կուրս ու բարձր, ու 1-2 տարիքային խումբ
            case 13:
            case 14:
            case 15:
                if (project.areStudents) {
                    if (person.category == Category.FIRST) {
                        availableExercises.add(6, 21, 22, 23);
                        availableExercises.add(25, 26, 27, 28);
                    } else {
                        availableExercises.add(6, 25, 26, 27);
                        availableExercises.add(28);
                    }
                } else {
                    if (ageGroupIndex == 14 || ageGroupIndex == 15) {
                        availableExercises.add(6, 25, 26, 27);
                        availableExercises.add(28);
                    }
                }
                break;
            // կին զինծառայողներ 3-րդ տարիքային խումբ ու բարձր
            case 16:
            case 17:
                availableExercises.add(6, 28);
                break;
        }
    }

    public void calculatePersonPoints(Person person) {
        int overallPoints = 0;

        for (Integer attachedExercise : person.attachedExercises) {
            if (!person.hasFilledRawValue(attachedExercise)) {
                continue;
            }
            int gradePoint;
            if (PhysTemplate.Instance().DataController().isFloatExercise(attachedExercise)) {
                float exerciseRawValue = person.getFloatExerciseRawValue(attachedExercise);
                gradePoint = getGradePointForExercise(attachedExercise, exerciseRawValue);
            } else {
                int intExerciseRawValue = person.getIntExerciseRawValue(attachedExercise);
                gradePoint = getGradePointForExercise(attachedExercise, intExerciseRawValue);
            }
            person.putExercisePoint(attachedExercise, gradePoint);
            overallPoints += gradePoint;
        }

        person.setOverallPoints(overallPoints);
    }

    private int getGradePointForExercise(int attachedExercise, float exerciseRawValue) {
        ObjectMap<Float, Integer> exerciseGradePoints = pointMap.get(attachedExercise);
        if (exerciseGradePoints == null) {
            throw new GdxRuntimeException("No points found for exercise " + attachedExercise);
        }

        Integer point = exerciseGradePoints.get(exerciseRawValue);
        if (point != null) {
            return point;
        }

        Array<Float> pointSortedArray = sortedExercisePointKey.get(attachedExercise);
        boolean arePointsDescending = loadedExercises.get(attachedExercise).arePointsDescending;

        int arraySize = pointSortedArray.size;
        if (arePointsDescending) {
            if (pointSortedArray.get(arraySize - 1) < exerciseRawValue) {
                return 0;
            }

        } else {
            if (pointSortedArray.first() > exerciseRawValue) {
                return 0;
            }
        }

        for (int i = 0; i < arraySize; i++) {
            Float f = pointSortedArray.get(i);
            if (f > exerciseRawValue) {
                int index = arePointsDescending ? i : i - 1;
                return exerciseGradePoints.get(pointSortedArray.get(index));
            }
        }

        return arePointsDescending ? exerciseGradePoints.get(pointSortedArray.first()) : exerciseGradePoints.get(pointSortedArray.get(arraySize - 1));
    }

    public void calculatePersonGrade(Person person) {
        ObjectMap<Category, IntMap<IntMap<Integer>>> ageGroup = gradeMap.get(person.ageGroup);
        if (ageGroup == null) {
            person.gradeCalculationError = new PersonGradeCalculationError(CalculationErrorType.MISSING_AGE_GROUP);
            person.canCalculateFinalGrade = false;
            return;
        }
        IntMap<IntMap<Integer>> categoryMap = ageGroup.get(person.category);
        if (categoryMap == null) {
            person.gradeCalculationError = new PersonGradeCalculationError(CalculationErrorType.MISSING_CATEGORY);
            person.canCalculateFinalGrade = false;
            return;
        }
        IntMap<Integer> exerciseCountMap = categoryMap.get(person.attachedExercises.size());
        if (exerciseCountMap == null) {
            person.gradeCalculationError = new PersonGradeCalculationError(CalculationErrorType.EXERCISE_COUNT);
            person.canCalculateFinalGrade = false;
            return;
        }

        person.gradeCalculationError = null;
        person.canCalculateFinalGrade = true;

        IntArray intArray = exerciseCountMap.keys().toArray();
        intArray.sort();

        if (intArray.first() > person.getOverallPoints()) {
            person.setGrade(Grade.BAD);
            return;
        }

        for (int i = 1; i < intArray.size; i++) {
            int iter = intArray.get(i);
            if (iter > person.getOverallPoints()) {
                person.setGrade(Grade.gradeTypeForGrade(exerciseCountMap.get(intArray.get(i - 1))));
                return;
            }
        }

        person.setGrade(Grade.EXCELLENT);

    }

    public Exercise getExerciseModelFor(int exerciseNumber) {
        return loadedExercises.get(exerciseNumber).copy();
    }

    public Array<Exercise> getAllExercises() {
        Array<Exercise> copyArray = new Array<>();
        for (Exercise value : loadedExercises.values()) {
            copyArray.add(value.copy());
        }

        return copyArray;
    }

    public Array<AgeGroup> getAllAgeGroups() {
        Array<AgeGroup> copyArray = new Array<>();
        for (AgeGroup loadedAgeGroup : loadedAgeGroups) {
            copyArray.add(loadedAgeGroup.copy());
        }

        return copyArray;
    }

    public boolean isFloatExercise(int number) {
        return getExerciseModelFor(number).isFloat;
    }

    public AgeGroup getAgeGroupFor(int ageGroupNumber) {
        for (AgeGroup loadedAgeGroup : loadedAgeGroups) {
            if (loadedAgeGroup.number == ageGroupNumber) {
                return loadedAgeGroup.copy();
            }
        }

        logger.error("No age group found for " + ageGroupNumber);
        return null;
    }

    public Array<Restriction> getAllRestrictions() {
        Array<Restriction> restrictions = new Array<>();
        for (Restriction loadedSoldierRestriction : loadedSoldierRestrictions) {
            restrictions.add(loadedSoldierRestriction.copy());
        }

        for (Restriction loadedOfficerRestriction : loadedOfficerRestrictions) {
            restrictions.add(loadedOfficerRestriction.copy());
        }
        return restrictions;
    }

    public Restriction getRestrictionFor(int restrictionIndex) {
        for (Restriction loadedOfficerRestriction : loadedOfficerRestrictions) {
            if (restrictionIndex == loadedOfficerRestriction.getIndex()) {
                return loadedOfficerRestriction.copy();
            }
        }

        for (Restriction loadedSoldierRestriction : loadedSoldierRestrictions) {
            if (restrictionIndex == loadedSoldierRestriction.getIndex()) {
                return  loadedSoldierRestriction.copy();
            }
        }

        return null;
    }
}


