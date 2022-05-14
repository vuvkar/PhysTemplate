package com.phys.template.views.peopleWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Exercise;
import com.phys.template.models.Person;
import com.phys.template.models.Restriction;

public class RestrictionGroupTable extends Table {

    private Person currentModifyingPerson;
    private Table mainTable = new Table();

    public RestrictionGroupTable() {
        defaults().pad(8);
        top().left();
        add(new VisLabel("Սահմանափակումներ", "big")).growX();
        row();
        add(mainTable).grow();
    }

    public void updateForPerson(final Person person) {
        mainTable.clear();

        mainTable.defaults().pad(8);
        mainTable.top().left();
        for (Restriction restriction : person.restrictions) {
            RestrictionWidget restrictionWidget = new RestrictionWidget(restriction, person, false);
            mainTable.add(restrictionWidget);
        }

        VisTextButton addRestriction = new VisTextButton("Ավելացնել \nսահմանափակում");
        addRestriction.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                PhysTemplate.Instance().UIStage().showRestrictionAddPopup(currentModifyingPerson);

            }
        });
        mainTable.add(addRestriction);

        this.currentModifyingPerson = person;
    }
}

class RestrictionWidget extends Table {

    private final VisLabel nameLabel;
    private final VisTextButton deleteButton;
    private final int restrictionIndex;

    public RestrictionWidget(Restriction restriction, final Person person, final boolean isAddition) {
        super();
        setSkin(PhysTemplate.Instance().UIStage().getSkin());
        restrictionIndex = restriction.getIndex();
        setBackground("border");
        nameLabel = new VisLabel();
        nameLabel.setText(restriction.getName());
        defaults().pad(5);
        add(nameLabel);
        row();
        String buttonText = isAddition ? "Ավելացնել":"Ջնջել";
        deleteButton = new VisTextButton(buttonText, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isAddition) {
                    PhysTemplate.Instance().ProjectController().getCurrentProject().addRestrictionTo(person, restrictionIndex);
                    PhysTemplate.Instance().UIStage().hideRestrictionPopup();
                } else {
                    PhysTemplate.Instance().ProjectController().getCurrentProject().removeRestrictionFrom(person, restrictionIndex);
                }
            }
        });
        add(deleteButton);
        pack();
    }
}
