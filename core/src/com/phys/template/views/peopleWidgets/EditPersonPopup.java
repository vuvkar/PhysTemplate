package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.util.FloatDigitsOnlyFilter;
import com.kotcrab.vis.ui.widget.*;
import com.phys.template.PhysTemplate;
import com.phys.template.models.*;

import java.util.ArrayList;

public class EditPersonPopup extends VisWindow {

    private VisTextField nameField;
    private VisTextField surnameField;
    private VisTextField fatherField;
    private VisSelectBox<Rank> rankSelectBox;
    private VisSelectBox<Sex> sexSelectBox;
    private VisSelectBox<Category> categorySelectBox;
    private VisSelectBox<AgeGroup> ageGroupSelectBox;
    private VisTextButton saveButton;
    private Table exercisesTable;
    private boolean isCreation;
    private int updatePersonIndex;

    private final ObjectMap<Exercise, VisTextField> exerciseMap = new ObjectMap<>();

    public EditPersonPopup() {
        super("Ավելացնել զինծառայող");
        setCenterOnAdd(true);
        setResizable(false);
        setMovable(true);
        addCloseButton();
        closeOnEscape();

        initContent();

        pack();
        invalidate();

        centerWindow();
        setSize(600, 600);
    }

    private void initContent() {
        Table mainTable = new Table();
        ScrollPane pane = new ScrollPane(mainTable);
        mainTable.defaults().pad(15);

        //rank config
        VisLabel rankLabel = new VisLabel("Կոչումը");
        mainTable.add(rankLabel);
        rankSelectBox = new VisSelectBox<>();
        rankSelectBox.setItems(Rank.values());
        mainTable.add(rankSelectBox);
        mainTable.row();

        //name config
        VisLabel nameLabel = new VisLabel("Անուն");
        mainTable.add(nameLabel);
        nameField = new VisTextField();
        mainTable.add(nameField);
        mainTable.row();

        //surname config
        VisLabel surnameConfig = new VisLabel("Ազգանուն");
        mainTable.add(surnameConfig);
        surnameField = new VisTextField();
        mainTable.add(surnameField);
        mainTable.row();

        //fathername config
        VisLabel fathernameConfig = new VisLabel("Հայրանուն");
        mainTable.add(fathernameConfig);
        fatherField = new VisTextField();
        mainTable.add(fatherField);
        mainTable.row();

        //sex config
        VisLabel sexLabel = new VisLabel("Սեռ");
        mainTable.add(sexLabel);
        sexSelectBox = new VisSelectBox<>();
        sexSelectBox.setItems(Sex.values());
        mainTable.add(sexSelectBox);
        mainTable.row();

        //age group
        VisLabel ageLabel = new VisLabel("Տարիքային խումբ");
        mainTable.add(ageLabel);
        ageGroupSelectBox = new VisSelectBox<>();
        ageGroupSelectBox.setItems(AgeGroup.values());
        mainTable.add(ageGroupSelectBox);
        mainTable.row();

        //category config
        VisLabel categoryLabel = new VisLabel("Կատեգորիա");
        mainTable.add(categoryLabel);
        categorySelectBox = new VisSelectBox<>();
        categorySelectBox.setItems(Category.values());
        mainTable.add(categorySelectBox);
        mainTable.row();

        exercisesTable = new Table();
        mainTable.add(exercisesTable).grow();

        pane.setScrollingDisabled(true, false);
        add(pane).grow();
        row();
        saveButton = new VisTextButton("Ավելացնել", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Person person = createPerson();
                person.index = updatePersonIndex;
                if (isCreation) {
                    PhysTemplate.Instance().ProjectController().addPersonToCurrentProject(person);
                } else {
                    PhysTemplate.Instance().ProjectController().updatePersonData(person);
                }
                PhysTemplate.Instance().UIStage().updatePeopleContent();
                PhysTemplate.Instance().UIStage().hidePersonAddPopup();
            }
        });
        add(saveButton);
    }

    public void updateForMode(boolean isCreation) {
        this.isCreation = isCreation;
        if (isCreation) {
            getTitleLabel().setText("Ավելացնել զինծառայող");
            nameField.clearText();
            surnameField.clearText();
            fatherField.clearText();
            categorySelectBox.setSelected(Category.FIRST);
            rankSelectBox.setSelected(Rank.SHN);
            sexSelectBox.setSelected(Sex.MALE);
            for (VisTextField value : exerciseMap.values()) {
                value.clearText();
            }
            saveButton.setText("Ավելացնել");
        } else {
            getTitleLabel().setText("Փոփոխել զինծառայողի տվյալները");
            saveButton.setText("Պահպանել");
        }
    }

    private Person createPerson() {
        // TODO: 11/22/2021 validate all data from fields

        Person person = new Person();
        person.name = nameField.getText();
        person.rank = rankSelectBox.getSelected();
        person.ageGroup = ageGroupSelectBox.getSelected();
        person.surname = surnameField.getText();
        person.fatherName = fatherField.getText();
        person.category = categorySelectBox.getSelected();
        person.sex = sexSelectBox.getSelected();

        for (ObjectMap.Entry<Exercise, VisTextField> entry : exerciseMap) {
            Exercise exercise = entry.key;
            VisTextField field = entry.value;
            person.addExercise(exercise.number);
            String text = field.getText();
            if (!text.isEmpty()) {
                person.putExerciseRawValue(exercise.number, Float.parseFloat(text));
            }
        }

        return person;
    }


    public void refreshExerciseContent(ArrayList<Exercise> exerciseList) {
        exercisesTable.clearChildren();
        exerciseMap.clear();

        for (Exercise exercise : exerciseList) {
            VisLabel exerciseName = new VisLabel(exercise.name);
            exercisesTable.add(exerciseName);
            VisTextField exerciseField = new VisTextField();
            exerciseField.setTextFieldFilter(new FloatDigitsOnlyFilter(false));
            exercisesTable.add(exerciseField);
            exercisesTable.row();
            exerciseMap.put(exercise, exerciseField);
        }
    }

    public void updateFor(Person person) {
        nameField.setText(person.name);
        surnameField.setText(person.surname);
        fatherField.setText(person.fatherName);
        rankSelectBox.setSelected(person.rank);
        sexSelectBox.setSelected(person.sex);
        categorySelectBox.setSelected(person.category);
        ageGroupSelectBox.setSelected(person.ageGroup);

        for (ObjectMap.Entry<Exercise, VisTextField> exerciseVisTextFieldEntry : exerciseMap) {
            VisTextField fi = exerciseVisTextFieldEntry.value;
//            float exerciseRawValue = person.getExerciseRawValue(exerciseVisTextFieldEntry.key.number);
//            fi.setText(String.valueOf(exerciseRawValue));
        }
        updatePersonIndex = person.index;
    }
}
