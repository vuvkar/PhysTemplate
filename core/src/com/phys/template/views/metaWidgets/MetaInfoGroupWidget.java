package com.phys.template.views.metaWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.phys.template.PhysTemplate;
import com.phys.template.UIStage;
import com.phys.template.models.Project;

public class MetaInfoGroupWidget extends Table {

    private Table mainContainer;
    private VisLabel headerLabel;

    private VisTextField baseNumberField;
    private VisTextField squadNameField;
    private VisCheckBox areStudentsCheckbox;
    private VisCheckBox isAviationCheckbox;

    public MetaInfoGroupWidget () {
        setSkin(PhysTemplate.Instance().UIStage().getSkin());
        setBackground("border");

        headerLabel = new VisLabel("Տվյալներ", "big");
        defaults().pad(10).top().left();
        add(headerLabel);
        row();

        baseNumberField = new VisTextField();
        squadNameField = new VisTextField();
        areStudentsCheckbox = new VisCheckBox("Կուրսանտներ");
        areStudentsCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: 5/3/2022 Final grade calculation
                // TODO: 5/3/2022 available exercises update
                boolean areStudents = areStudentsCheckbox.isChecked();
                Project currentProject = PhysTemplate.Instance().ProjectController().getCurrentProject();
                currentProject.setStudents(areStudents);
                PhysTemplate.Instance().UIStage().updateContent();
            }
        });
        isAviationCheckbox = new VisCheckBox("Ավիացիոն ստորաբաժ.");
        isAviationCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: 5/3/2022 available exercises update
                boolean isAviation = isAviationCheckbox.isChecked();
                Project currentProject = PhysTemplate.Instance().ProjectController().getCurrentProject();
                currentProject.setAviation(isAviation);
            }
        });

        mainContainer = new Table();
        mainContainer.defaults().padRight(10);
        mainContainer.top().left();
        updateContent();

        add(mainContainer).grow();
    }

    public void updateContent() {
        mainContainer.clearChildren();

        Table baseCell = new Table();
        baseCell.defaults().left().pad(5);
        baseCell.add(new VisLabel("Զորամասի \nհամար"));
        baseCell.row();
        baseCell.add(baseNumberField);
        baseCell.row();
        baseCell.add(areStudentsCheckbox);
        mainContainer.add(baseCell);

        Table squadCell = new Table();
        squadCell.defaults().left().pad(5);
        squadCell.add(new VisLabel("Ստորաբաժանման \nանվանում"));
        squadCell.row();
        squadCell.add(squadNameField).growX();
        squadCell.row();
        squadCell.add(isAviationCheckbox);
        mainContainer.add(squadCell).growX();
    }

    public String getSquadName() {
        return squadNameField.getText();
    }

    public String getBaseNumber () {
        return baseNumberField.getText();
    }

}
