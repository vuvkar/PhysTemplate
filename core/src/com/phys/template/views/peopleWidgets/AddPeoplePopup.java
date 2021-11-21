package com.phys.template.views.peopleWidgets;

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

    }
}
