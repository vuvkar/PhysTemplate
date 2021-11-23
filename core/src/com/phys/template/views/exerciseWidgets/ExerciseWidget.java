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
                // TODO: 11/19/2021 handle deletion
                PhysTemplate.Instance().ProjectController().removeExercise(ExerciseWidget.this.exerciseNumber);
                PhysTemplate.Instance().UIStage().updateExerciseContent();
            }
        });
        add(deleteButton);
        pack();
    }

    public void setName (String name) {
        nameLabel.setText(name);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
