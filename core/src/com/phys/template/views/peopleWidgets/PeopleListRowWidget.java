package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Person;

public class PeopleListRowWidget extends Table {

    private VisImageButton moveTop;
    private VisImageButton moveDown;
    private VisImage invalidRowImage;

    private VisLabel rank;
    private VisLabel name;
    private VisLabel sex;
    private VisLabel ageGroup;
    private VisLabel category;
    private VisLabel finalPoints;
    private VisLabel finalGrade;

    private Person person;


    public PeopleListRowWidget() {
        super();
        // TODO: 11/22/2021 handle validation UI, if some exercises are missing info
        Skin skin = PhysTemplate.Instance().UIStage().getSkin();
        moveTop = new VisImageButton(skin.getDrawable("select-up"));
        moveTop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (person.index != 0) {
                    PhysTemplate.Instance().ProjectController().movePersonUp(person);
                    PhysTemplate.Instance().UIStage().updatePeopleContent();
                }
            }
        });
        moveDown = new VisImageButton(skin.getDrawable("select-down"));
        moveDown.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (person.index != PhysTemplate.Instance().ProjectController().getPeopleCount() - 1) {
                    PhysTemplate.Instance().ProjectController().movePersonDown(person);
                    PhysTemplate.Instance().UIStage().updatePeopleContent();
                }
            }
        });
        Table buttonsTable = new Table();
        buttonsTable.add(moveTop);
        buttonsTable.row();
        buttonsTable.add(moveDown);

        defaults().pad(5);
        left().top();
        add(buttonsTable);
        rank = new VisLabel();
        add(rank);
        name = new VisLabel();
        add(name).growX();
        sex = new VisLabel();
        add(sex);
        ageGroup = new VisLabel();
        add(ageGroup);
        category = new VisLabel();
        add(category);
        finalPoints = new VisLabel();
        add(finalPoints);
        finalGrade = new VisLabel();
        add(finalGrade);
    }

    public void updateForPerson (Person person) {
        rank.setText(person.rank.toString());
        name.setText(person.getFullName());
        sex.setText(person.sex.toString());
        ageGroup.setText(person.ageGroup.toString());
        category.setText(person.category.toString());
        finalPoints.setText(50);
        finalGrade.setText(5);
        this.person = person;
    }

}
