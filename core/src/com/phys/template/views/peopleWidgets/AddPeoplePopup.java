package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;
import com.phys.template.models.*;
import com.phys.template.views.exerciseWidgets.ExercisePreviewWidget;

import java.util.ArrayList;

public class AddPeoplePopup extends VisWindow {
    private VisTextField nameField;
    private VisTextField surnameField;
    private VisTextField fatherField;
    private VisSelectBox<Rank> rankSelectBox;
    private VisSelectBox<Sex> sexSelectBox;
    private VisSelectBox<Category> categorySelectBox;
    private VisSelectBox<AgeGroup> ageGroupSelectBox;
    private Table exercisesTable;

    public AddPeoplePopup() {
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
        mainTable.add(exercisesTable);

        pane.setScrollingDisabled(true, false);
        add(pane).grow();
        row();
        VisTextButton saveButton = new VisTextButton("Ավելացնել", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        add(saveButton);
    }

    public void refreshContent(ArrayList<Person> peopleList) {

    }

    public void refreshExerciseContent(ArrayList<Exercise> exerciseList) {

    }
}
