package com.phys.template.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import com.phys.template.models.*;

public class DataController {
    private final JsonReader json = new JsonReader();


    // AgeGroup -> Category -> ExerciseCount -> Point -> Grade
    private final ObjectMap<AgeGroup, ObjectMap<Category, IntMap<IntMap<Integer>>>> gradeMap = new ObjectMap<>();

    private final IntMap<Exercise> loadedExercises = new IntMap<>();

    private final IntMap<ObjectMap<Float, Integer>> pointMap = new IntMap<>();
    private final IntMap<Array<Float>> sortedExercisePointKey = new IntMap<>();

    private final Array<AgeGroup> loadedAgeGroups = new Array<>();

    public DataController() {
        loadExercisesData();
        loadExercisePointData();
        loadGradePointData();
    }

    private void loadExercisesData() {
        JsonValue base = json.parse(Gdx.files.internal("exercises.json"));

        for (JsonValue jsonValue : base) {
            Exercise exercise = new Exercise();
            exercise.number = jsonValue.getInt("number");
            exercise.longName = jsonValue.getString("longName");
            exercise.name = jsonValue.getString("name");

            loadedExercises.put(exercise.number, exercise);
        }
    }

    public void loadExercisePointData () {
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
                IntMap<Integer> gradeMap = new IntMap<>();
                for (JsonValue exerciseCountJson : gradePointsJson) {
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
                    ageGroupObjectMap.put(categories[ord-1], categoryObjectMap);
                }
            }

        }
    }

    public void loadGradePointData () {
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

    public void calculatePersonPoints(Person person) throws PersonGradeCalculationError {
        int overallPoints = 0;

        for (Integer attachedExercise : person.attachedExercises) {
            float exerciseRawValue = person.getExerciseRawValue(attachedExercise);
            int gradePoint = getGradePointForExercise(attachedExercise, exerciseRawValue);
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
        if (pointSortedArray.first() > exerciseRawValue) {
            return 0;
        }

        int arraySize = pointSortedArray.size;
        for (int i = 0; i < arraySize; i++) {
            Float f = pointSortedArray.get(i);
            if (f > exerciseRawValue) {
                return exerciseGradePoints.get(pointSortedArray.get(i-1));
            }
        }

        return exerciseGradePoints.get(pointSortedArray.get(arraySize - 1));
    }

    public void calculatePersonGrade(Person person) throws PersonGradeCalculationError {
        ObjectMap<Category, IntMap<IntMap<Integer>>> ageGroup = gradeMap.get(person.ageGroup);
        if (ageGroup == null) {
            throw new PersonGradeCalculationError(CalculationErrorType.MISSING_AGE_GROUP);
        }
        IntMap<IntMap<Integer>> categoryMap = ageGroup.get(person.category);
        if (categoryMap == null) {
            throw new PersonGradeCalculationError(CalculationErrorType.MISSING_CATEGORY);
        }
        IntMap<Integer> exerciseCountMap = categoryMap.get(person.attachedExercises.size());
        if (exerciseCountMap == null) {
            throw new PersonGradeCalculationError(CalculationErrorType.EXERCISE_COUNT);
        }

        IntArray intArray = exerciseCountMap.keys().toArray();
        intArray.sort();

        int ceil = getCeil(intArray, person.getOverallPoints());
        if (ceil == -1) {
            person.setGrade(2);
        } else {
            Integer grade = exerciseCountMap.get(ceil);
            if (grade == null) {
                throw  new PersonGradeCalculationError(CalculationErrorType.MISSING_GRADE);
            }
            person.setGrade(grade);
        }
    }

    public static int getCeil(IntArray nums, int x)
    {
        // search space is nums[left…right]
        int left = 0, right = nums.size - 1;

        // initialize ceil to -1
        int ceil = -1;

        // loop till the search space is exhausted
        while (left <= right)
        {
            // find the mid-value in the search space
            int mid = (left + right) / 2;

            // if `x` is equal to the middle element, it is the ceil
            if (nums.get(mid) == x) {
                return nums.get(mid);
            }

            // if `x` is less than the middle element, the ceil exists in the
            // subarray nums[left…mid]; update ceil to the middle element
            // and reduce our search space to the left subarray nums[left…mid-1]
            else if (x < nums.get(mid))
            {
                ceil = nums.get(mid);
                right = mid - 1;
            }

            // if `x` is more than the middle element, the ceil exists in the
            // right subarray nums[mid+1…right]
            else {
                left = mid + 1;
            }
        }

        return ceil;
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

    public Array<AgeGroup> getAllAgeGroups () {
        Array<AgeGroup> copyArray = new Array<>();
        for (AgeGroup loadedAgeGroup : loadedAgeGroups) {
            copyArray.add(loadedAgeGroup.copy());
        }

        return copyArray;
    }
}


