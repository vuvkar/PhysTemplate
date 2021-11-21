package com.phys.template.views.exerciseWidgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ExerciseWidget extends Table {

    private VisLabel nameLabel;
    private VisTextButton deleteButton;

    @Override
    public float getPrefHeight() {
        return 100;
    }

    public ExerciseWidget() {
        super();
        nameLabel = new VisLabel();
        add(nameLabel);
        row();
        deleteButton = new VisTextButton("Ջնջել", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: 11/19/2021 handle deletion
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
