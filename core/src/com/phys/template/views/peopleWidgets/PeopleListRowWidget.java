package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Exercise;
import com.phys.template.models.Grade;
import com.phys.template.models.Person;

public class PeopleListRowWidget extends Table {

    public static int NUMBER_LENGTH = 40;
    public static int RANK_LENGTH = 60;
    public static int NAME_LENGTH = 400;
    public static int SEX_LENGTH = 80;
    public static int AGE_GROUP_LENGTH = 90;
    public static int CATEGORY_LENGTH = 100;
    public static int POINTS_LENGTH = 90;
    public static int FINAL_GRADE_LENGTH = 120;
    public static int EXERCISE_COLUMN_LENGTH = 120;

    private VisImage invalidRowImage;

    private final RowCell index;
    private final RowCell rank;
    private final RowCell name;
    private final RowCell sex;
    private final RowCell ageGroup;
    private final RowCell category;
    private final RowCell finalPoints;
    private final RowCell finalGrade;
    private final Array<RowCell> exerciseRowCells;
    private final Table exerciseTable;

    private Person person;


    public PeopleListRowWidget() {
        this(false);
    }

    public PeopleListRowWidget(boolean isFirstRow) {
        super();
        left().top();
        defaults().padRight(2);
        exerciseRowCells = new Array<>();
        exerciseTable = new Table();
        if (isFirstRow) {
            index = new RowCell(NUMBER_LENGTH, true);
            index.setText("Հ/Հ");
            add(index).growY();
            rank = new RowCell(RANK_LENGTH, true);
            rank.setText("Կոչում");
            add(rank).growY();
            name = new RowCell(NAME_LENGTH, true);
            add(name).grow();
            name.setText("Ա.Ա.Հ.");
            sex = new RowCell(SEX_LENGTH, true);
            add(sex).growY();
            sex.setText("Սեռ");
            ageGroup = new RowCell(AGE_GROUP_LENGTH, true);
            add(ageGroup).growY();
            ageGroup.setText("Տարիքային խումբ");
            category = new RowCell(CATEGORY_LENGTH, true);
            add(category).growY();
            category.setText("Կատեգորիա");
            add(exerciseTable).growY();
            finalPoints = new RowCell(POINTS_LENGTH, true);
            add(finalPoints).growY();
            finalPoints.setText("Ընդհ. միավորներ");
            finalPoints.setWrap(true);
            finalGrade = new RowCell(FINAL_GRADE_LENGTH, true);
            add(finalGrade).growY();
            finalGrade.setText("Վերջնական գնահատական");
            finalGrade.setWrap(true);
            setBackground(PhysTemplate.Instance().UIStage().getSkin().getDrawable("slider-knob"));

        } else {
            // TODO: 11/22/2021 handle validation UI, if some exercises are missing info
            index = new RowCell(NUMBER_LENGTH);
            add(index).growY();
            rank = new RowCell(RANK_LENGTH);
            add(rank).growY();
            name = new RowCell(NAME_LENGTH);
            add(name).grow();
            sex = new RowCell(SEX_LENGTH);
            add(sex).growY();
            ageGroup = new RowCell(AGE_GROUP_LENGTH);
            add(ageGroup).growY();
            category = new RowCell(CATEGORY_LENGTH);
            add(category).growY();
            add(exerciseTable).growY();
            finalPoints = new RowCell(POINTS_LENGTH);
            add(finalPoints).growY();
            finalGrade = new RowCell(FINAL_GRADE_LENGTH);
            add(finalGrade).growY();
            addListeners();
            setBackground(PhysTemplate.Instance().UIStage().getSkin().getDrawable("slider-knob"));
        }
    }

    private void addListeners() {
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                PhysTemplate.Instance().UIStage().showEditPersonPopup(person);
            }
        });
    }

    public void updateTopRowForExercises(Array<Exercise> exercises) {
        exerciseTable.clearChildren();
        for (Exercise exercise : exercises) {
            RowCell rawValue = new RowCell(EXERCISE_COLUMN_LENGTH);
            exerciseTable.add(rawValue).padRight(2).growY();
            rawValue.setText("Վարժ. " + exercise.number + "\n արդյունք/բալ");
        }
    }

    public void updateForPerson(Person person) {
        index.setText(person.index + 1);
        rank.setText(person.rank.shortName());
        name.setText(person.getFullName());
        sex.setText(person.sex.toString());
        ageGroup.setText(person.ageGroup.toString());
        category.setText(person.category.toString());
        exerciseTable.clearChildren();
        for (Integer attachedExercise : person.attachedExercises) {
            String valueText = "";
            RowCell value = new RowCell(EXERCISE_COLUMN_LENGTH);
            exerciseTable.add(value).spaceRight(2).growY();
            if (person.hasFilledRawValue(attachedExercise)) {
                if (PhysTemplate.Instance().DataController().isFloatExercise(attachedExercise)) {
                    String rawValue = String.valueOf(person.getFloatExerciseRawValue(attachedExercise));
                    valueText += rawValue;
                } else {
                    String rawValue = String.valueOf(person.getIntExerciseRawValue(attachedExercise));
                    valueText += rawValue;
                }
            } else {
                valueText += "-";
            }
            valueText += "/";

            if (person.hasFilledRawValue(attachedExercise)) {
                String pointValueText = String.valueOf(person.getExercisePoint(attachedExercise));
                valueText += pointValueText;
            } else {
                valueText += "-";
            }
            value.setText(valueText);
        }

        finalPoints.setText(person.getOverallPoints());
        if (person.canCalculateFinalGrade) {
            Grade grade = person.getGrade();
            finalGrade.setText(grade.getNumericalGrade() + " " + grade.getDescription(true, true));
        } else {
            finalGrade.setText("-");
        }
        this.person = person;
    }

    private static class RowCell extends VisLabel {
        int length;

        public RowCell(int cellLength) {
            this(cellLength, false);
        }

        public RowCell(int cellLength, boolean isFirstRow) {
            super();
            LabelStyle style = getStyle();
            // TODO: 11/24/2021 optimize to have only one
            LabelStyle labelStyle = new LabelStyle();
            labelStyle.background = PhysTemplate.Instance().UIStage().getSkin().getDrawable("slider-knob-disabled");
            labelStyle.font = style.font;
            labelStyle.fontColor = style.fontColor;
            setStyle(labelStyle);
            length = cellLength;
            setAlignment(Alignment.CENTER.getAlignment());
            setWidth(cellLength);
            if (isFirstRow) {
                setWrap(true);
            } else {
                setEllipsis(true);
            }
        }

        @Override
        public float getPrefWidth() {
            return length;
        }
    }

}
