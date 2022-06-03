package com.phys.template.views.metaWidgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.phys.template.PhysTemplate;
import com.phys.template.input.KeyboardHandledTextField;
import com.phys.template.input.KeyboardType;
import com.phys.template.models.Metadata;
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

        baseNumberField = new KeyboardHandledTextField("", PhysTemplate.Instance().UIStage().getPlatformSpecificManager(), KeyboardType.NUMERIC);
        squadNameField = new KeyboardHandledTextField("", PhysTemplate.Instance().UIStage().getPlatformSpecificManager(), KeyboardType.TEXT);
        baseNumberField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Project currentProject = PhysTemplate.Instance().ProjectController().getCurrentProject();
                currentProject.getMetadata().setBaseNumber(baseNumberField.getText());
            }
        });

        squadNameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Project currentProject = PhysTemplate.Instance().ProjectController().getCurrentProject();
                currentProject.getMetadata().setSquadName(squadNameField.getText());
            }
        });
        areStudentsCheckbox = new VisCheckBox("Կուրսանտներ");
        areStudentsCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
                boolean isAviation = isAviationCheckbox.isChecked();
                Project currentProject = PhysTemplate.Instance().ProjectController().getCurrentProject();
                currentProject.setAviation(isAviation);
                PhysTemplate.Instance().UIStage().updateContent();
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

    public void refreshContent() {
        Project currentProject = PhysTemplate.Instance().ProjectController().getCurrentProject();
        Metadata metadata = currentProject.getMetadata();
        squadNameField.setText(metadata.getSquadName());
        baseNumberField.setText(metadata.getBaseName());
        isAviationCheckbox.setChecked(currentProject.isAviation);
        areStudentsCheckbox.setChecked(currentProject.areStudents);
    }
}
