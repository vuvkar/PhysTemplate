package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Exercise;
import com.phys.template.models.Person;
import com.phys.template.models.Restriction;
import com.phys.template.views.exerciseWidgets.ExercisePreviewWidget;

public class AddRestrictionPopup extends VisWindow {
    private GridGroup widgetGrid;

    public AddRestrictionPopup() {
        super("Ավելացնել Սահմանափակում");

        setCenterOnAdd(true);
        setResizable(false);
        setMovable(true);
        addCloseButton();
        closeOnEscape();

        initContent();

        pack();
        invalidate();

        centerWindow();
        setSize(350, 500);
    }

    private void initContent() {
        widgetGrid = new GridGroup();
        widgetGrid.setItemSize(100, 100);
        ScrollPane scrollPane = new ScrollPane(widgetGrid);
        scrollPane.setScrollingDisabled(true, false);
        add(scrollPane).grow();
    }

    public void refreshContent(Person person) {
        widgetGrid.clear();
        Array<Restriction> availableRestrictions = PhysTemplate.Instance().ProjectController().getAvailableRestrictionsFor(person);

        for (Restriction availableRestriction : availableRestrictions) {
            RestrictionWidget exercisePreviewWidget = new RestrictionWidget(availableRestriction, person, true);
            widgetGrid.addActor(exercisePreviewWidget);
        }
        widgetGrid.invalidate();
    }
}
