package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Person;

import java.util.ArrayList;

public class PeopleListWidget extends Table {
    private VisLabel nameLabel;
    private ScrollPane scrollPane;
    private VisTextButton addPerson;
    private VerticalGroup mainGroup;

    public PeopleListWidget() {
        super();
        nameLabel = new VisLabel("Անվանացուցակ");
        top().left();
        pad(20);

        add(nameLabel).left();
        row();
        mainGroup = new VerticalGroup();
        mainGroup.pad(10);
        mainGroup.space(10);

        updateContent();
        scrollPane = new ScrollPane(mainGroup);
        scrollPane.setScrollingDisabled(true, false);
        add(scrollPane).grow();

        row();
        addPerson = new VisTextButton("Նոր զինծառայող", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: 11/19/2021 handle people creation
            }
        });
        add(addPerson).growX();
    }

    public void updateContent() {
        mainGroup.clearChildren();
        // TODO: 11/19/2021 handle top row
        ArrayList<Person> currentProjectPeople = PhysTemplate.Instance().ProjectController().getCurrentProjectPeople();
        for (Person currentProjectPerson : currentProjectPeople) {
            PeopleListRowWidget peopleListRowWidget = new PeopleListRowWidget();
            mainGroup.addActor(peopleListRowWidget);
        }
    }
}
