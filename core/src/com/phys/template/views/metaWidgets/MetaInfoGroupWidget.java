package com.phys.template.views.metaWidgets;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.phys.template.PhysTemplate;
import com.phys.template.UIStage;

public class MetaInfoGroupWidget extends Table {

    private Table mainContainer;
    private VisLabel headerLabel;

    private VisTextField baseNumberField;
    private VisTextField squadNameField;

    public MetaInfoGroupWidget () {
        setSkin(PhysTemplate.Instance().UIStage().getSkin());
        setBackground("border");

        headerLabel = new VisLabel("Տվյալներ", "big");
        defaults().pad(10).top().left();
        add(headerLabel);
        row();

        baseNumberField = new VisTextField();
        squadNameField = new VisTextField();

        mainContainer = new Table();
        mainContainer.defaults().padRight(10);
        mainContainer.top().left();
        updateContent();

        add(mainContainer).grow().height(UIStage.TOP_PART_HEIGHT);
    }

    public void updateContent() {
        mainContainer.clearChildren();

        Table baseCell = new Table();
        baseCell.defaults().left().pad(5);
        baseCell.add(new VisLabel("Զորամասի \nհամար"));
        baseCell.row();
        baseCell.add(baseNumberField);
        mainContainer.add(baseCell);

        Table squadCell = new Table();
        squadCell.defaults().left().pad(5);
        squadCell.add(new VisLabel("Ստորաբաժանման \nանվանում"));
        squadCell.row();
        squadCell.add(squadNameField).growX();
        mainContainer.add(squadCell).growX();
    }

    public String getSquadName() {
        return squadNameField.getText();
    }

    public String getBaseNumber () {
        return baseNumberField.getText();
    }

}
