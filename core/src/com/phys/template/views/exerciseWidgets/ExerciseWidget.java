package com.phys.template.views.exerciseWidgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.phys.template.PhysTemplate;

public class ExerciseWidget extends Table {

    private VisLabel nameLabel;
    private VisTextButton deleteButton;
    private int exerciseNumber;

    public ExerciseWidget(int exerciseNumber) {
        super();
        setSkin(PhysTemplate.Instance().UIStage().getSkin());
        setBackground("border");
        nameLabel = new VisLabel();
        defaults().pad(5);
        this.exerciseNumber = exerciseNumber;
        add(nameLabel);
        row();
        deleteButton = new VisTextButton("Ջնջել", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PhysTemplate.Instance().ProjectController().removeExercise(ExerciseWidget.this.exerciseNumber);
            }
        });
        add(deleteButton).height(60);
        pack();
    }

    public void setName (String name) {
        nameLabel.setText(name);
    }

}
