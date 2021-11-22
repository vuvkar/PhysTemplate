package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
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


    public PeopleListRowWidget() {
        // TODO: 11/22/2021 handle validation UI, if some exercises are missing info
        super();
        defaults().pad(5);
        left().top();
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
    }

}
