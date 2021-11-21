package com.phys.template.views.exerciseWidgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Exercise;

public class ExercisePreviewWidget extends Table {

    private VisLabel nameLabel;
    private VisLabel descriptionLabel;

    private int exerciseNumber;

    public ExercisePreviewWidget(Exercise exercise) {
        super();
        exerciseNumber = exercise.number;
        nameLabel = new VisLabel(exercise.name);
        descriptionLabel = new VisLabel(exercise.longName);

        add(nameLabel).growX();
        row();
        add(descriptionLabel).grow();
        pack();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (event.getButton() == Input.Buttons.LEFT) {
                    PhysTemplate.Instance().ProjectController().addExerciseToCurrentProject(exerciseNumber);
                    PhysTemplate.Instance().UIStage().updateExerciseContent();
                    PhysTemplate.Instance().UIStage().hideExerciseAddPopup();
                }
            }
        });
    }
}
