package com.phys.template.views.exerciseWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.phys.template.PhysTemplate;
import com.phys.template.UIStage;
import com.phys.template.models.Exercise;

import java.util.ArrayList;

public class ExercisesGroupWidget extends Table {
    Table mainContainer;
    VisLabel headerLabel;

    public ExercisesGroupWidget() {
        super();
        setSkin(PhysTemplate.Instance().UIStage().getSkin());
        setBackground("border");

        headerLabel = new VisLabel("Անցկացվելիք վարժություններ", "big");
        defaults().pad(10).top().left();
        add(headerLabel);
        row();

        mainContainer = new Table();
        mainContainer.defaults().padRight(10);
        mainContainer.top().left();
        updateContent();

        add(mainContainer).grow();
    }

    public void updateContent() {
        mainContainer.clearChildren();
        Array<Exercise> currentProjectExercises = PhysTemplate.Instance().ProjectController().getCurrentProjectExercises();
        for (Exercise currentProjectExercise : currentProjectExercises) {
            ExerciseWidget exerciseWidget = new ExerciseWidget(currentProjectExercise.number);
            exerciseWidget.setName(currentProjectExercise.name);
            mainContainer.add(exerciseWidget);
        }

        VisTextButton addExercise = new VisTextButton("Ավելացնել \nվարժություն");
        addExercise.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PhysTemplate.Instance().UIStage().showExerciseAddPopup();

            }
        });
        mainContainer.add(addExercise);
    }
}
