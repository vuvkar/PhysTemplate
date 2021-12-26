package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private Table mainPeopleTable;
    private Table mainTable;
    public PeopleListRowWidget topRow;

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
        mainTable = new Table();
        createFirstRow();
        mainTable.row();
        mainPeopleTable = new Table();
        mainPeopleTable.top().left();
        mainPeopleTable.padTop(30);
        mainPeopleTable.defaults().padBottom(10);

        updateContent();
        scrollPane = new ScrollPane(mainPeopleTable);
        scrollPane.setScrollingDisabled(false, false);
        mainTable.add(scrollPane).grow();

        ScrollPane mainScrollPane = new ScrollPane(mainTable);
        mainScrollPane.setScrollingDisabled(false, true);
        add(mainScrollPane).growY();

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
        topRow = new PeopleListRowWidget(true);
        mainTable.add(topRow).padTop(5).height(90);
    }

    public void updateContent() {
        mainPeopleTable.clearChildren();
        // TODO: 11/19/2021 handle top row
        ArrayList<Person> currentProjectPeople = PhysTemplate.Instance().ProjectController().getCurrentProjectPeople();
        for (Person currentProjectPerson : currentProjectPeople) {
            PeopleListRowWidget peopleListRowWidget = new PeopleListRowWidget();
            peopleListRowWidget.updateForPerson(currentProjectPerson);
            mainPeopleTable.add(peopleListRowWidget).growX();
            mainPeopleTable.row();
        }
    }
}
