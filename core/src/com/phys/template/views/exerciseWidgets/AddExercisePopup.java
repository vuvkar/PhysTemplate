package com.phys.template.views.exerciseWidgets;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Exercise;

import java.util.ArrayList;

public class AddExercisePopup extends VisWindow {

    private GridGroup widgetGrid;

    public AddExercisePopup() {
        super("Ավելացնել վարժություն");

        setCenterOnAdd(true);
        setResizable(false);
        setMovable(true);
        addCloseButton();
        closeOnEscape();

        initContent();

        pack();
        invalidate();

        centerWindow();
        setSize(1280, 720);
    }

    private void initContent() {
        widgetGrid = new GridGroup();
        widgetGrid.setItemSize(400, 180);
        ScrollPane scrollPane = new ScrollPane(widgetGrid);
        scrollPane.setScrollingDisabled(true, false);
        add(scrollPane).grow();
    }

    public void refreshContent() {
        widgetGrid.clear();
        Array<Exercise> availableExercises = PhysTemplate.Instance().ProjectController().getAvailableExercises();

        for (Exercise availableExercise : availableExercises) {
            ExercisePreviewWidget exercisePreviewWidget = new ExercisePreviewWidget(availableExercise);
            widgetGrid.addActor(exercisePreviewWidget);
        }
        widgetGrid.invalidate();
    }

}
