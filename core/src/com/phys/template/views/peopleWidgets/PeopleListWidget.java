package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
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
        defaults().top().left();

        add(nameLabel).left();
        row();
        mainTable = new Table();
        createFirstRow();
        mainTable.row();
        mainPeopleTable = new Table();
        mainPeopleTable.top().left();
        mainPeopleTable.padTop(15);
        mainPeopleTable.defaults().padBottom(6);
        mainTable.add(mainPeopleTable).grow();

        updateContent();
        scrollPane = new VisScrollPane(mainTable);
        scrollPane.setScrollingDisabled(false, false);

        add(scrollPane).grow();

        row();
        addPerson = new VisTextButton("Նոր զինծառայող", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PhysTemplate.Instance().UIStage().showPersonAddPopup();
            }
        });
        add(addPerson).padTop(5).growX();
    }

    private void createFirstRow() {
        topRow = new PeopleListRowWidget(true);
        mainTable.add(topRow).padTop(5).height(60);
    }

    public void updateContent() {
        mainPeopleTable.clearChildren();
        ArrayList<Person> currentProjectPeople = PhysTemplate.Instance().ProjectController().getCurrentProjectPeople();
        for (Person currentProjectPerson : currentProjectPeople) {
            PeopleListRowWidget peopleListRowWidget = new PeopleListRowWidget();
            peopleListRowWidget.updateForPerson(currentProjectPerson);
            mainPeopleTable.add(peopleListRowWidget).growX();
            mainPeopleTable.row();
        }
    }
}
