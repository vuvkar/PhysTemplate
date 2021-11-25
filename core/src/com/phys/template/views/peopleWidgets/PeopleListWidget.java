package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    private Table mainGroup;

    public PeopleListWidget() {
        super();
        Skin skin = PhysTemplate.Instance().UIStage().getSkin();
        setSkin(skin);
        setBackground("border");
        nameLabel = new VisLabel("Անձնակազմի անվանացուցակ", "big");
        top().left();
        pad(10);

        add(nameLabel).left();
        row();
        createFirstRow();
        row();
        mainGroup = new Table();
        mainGroup.top().left();
        mainGroup.padTop(30);
        mainGroup.defaults().padBottom(10);

        updateContent();
        scrollPane = new ScrollPane(mainGroup);
        scrollPane.setScrollingDisabled(true, false);
        add(scrollPane).grow();

        row();
        addPerson = new VisTextButton("Նոր զինծառայող", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PhysTemplate.Instance().UIStage().showPersonAddPopup();
            }
        });
        add(addPerson).growX();
    }

    private void createFirstRow() {
        PeopleListRowWidget peopleListRowWidget = new PeopleListRowWidget(true);
        add(peopleListRowWidget).growX().padTop(5).height(90);
    }

    public void updateContent() {
        mainGroup.clearChildren();
        // TODO: 11/19/2021 handle top row
        ArrayList<Person> currentProjectPeople = PhysTemplate.Instance().ProjectController().getCurrentProjectPeople();
        for (Person currentProjectPerson : currentProjectPeople) {
            PeopleListRowWidget peopleListRowWidget = new PeopleListRowWidget();
            peopleListRowWidget.updateForPerson(currentProjectPerson);
            mainGroup.add(peopleListRowWidget).growX();
            mainGroup.row();
        }
    }
}
