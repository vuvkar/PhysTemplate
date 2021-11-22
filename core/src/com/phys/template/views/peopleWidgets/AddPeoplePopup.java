package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class AddPeoplePopup extends VisWindow {
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
        setSize(800, 500);
    }

    private void initContent() {
        VisLabel nameLabel = new VisLabel("Անուն։ ");
        add(nameLabel);
        VisTextField nameField = new VisTextField();
        add(nameField);
        row();
        VisTextButton saveButton = new VisTextButton("Ավելացնել", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        add(saveButton);
    }

    public void refreshContent() {

    }
}
