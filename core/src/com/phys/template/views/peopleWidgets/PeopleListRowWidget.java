package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;

public class PeopleListRowWidget extends Table {

    private VisImageButton moveTop;
    private VisImageButton moveDown;
    private VisLabel name;
    private VisLabel rank;

    public PeopleListRowWidget() {
        super();
        name = new VisLabel("Զաքարյան Արմեն Ավետիքի");
        add(name).grow();
    }

}
