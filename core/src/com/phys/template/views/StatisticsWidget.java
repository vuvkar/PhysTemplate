package com.phys.template.views;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.phys.template.PhysTemplate;
import com.phys.template.UIStage;
import com.phys.template.controllers.ProjectController;
import com.phys.template.models.Project;


public class StatisticsWidget extends Table {

    private Table mainContainer;
    private VisLabel headerLabel;

    private VisLabel allNumber = new VisLabel();
    private VisLabel excellent = new VisLabel();
    private VisLabel good = new VisLabel();
    private VisLabel ok = new VisLabel();
    private VisLabel bad = new VisLabel();
    private VisLabel overallOks = new VisLabel();
    private VisLabel getFreed = new VisLabel();

    public StatisticsWidget () {
        setSkin(PhysTemplate.Instance().UIStage().getSkin());
        setBackground("border");

        headerLabel = new VisLabel("Ընդհանուր", "big");
        defaults().pad(10).top().left();
        add(headerLabel);
        row();

        mainContainer = new Table();
        mainContainer.defaults().padRight(10);
        mainContainer.top().left();
        updateContent();
        refreshContent();

        add(mainContainer).grow();
    }

    public void updateContent() {
        mainContainer.clearChildren();

        mainContainer.defaults().left().pad(5);

        mainContainer.add(new VisLabel("Ստուգվել է - "));
        mainContainer.add(allNumber);
        mainContainer.row();

        mainContainer.add(new VisLabel("Գերազանց - "));
        mainContainer.add(excellent);
        mainContainer.row();

        mainContainer.add(new VisLabel("Լավ - "));
        mainContainer.add(good);
        mainContainer.row();

        mainContainer.add(new VisLabel("Բավարար - "));
        mainContainer.add(ok);
        mainContainer.row();

        mainContainer.add(new VisLabel("Անբավարար - "));
        mainContainer.add(bad);
        mainContainer.row();

        mainContainer.add(new VisLabel("Դրական են \nստացել - "));
        mainContainer.add(overallOks);
        mainContainer.row();

        mainContainer.add(new VisLabel("Ազատվել են - "));
        mainContainer.add(getFreed);
        mainContainer.row();
    }

    public void refreshContent() {
        ProjectController projectController = PhysTemplate.Instance().ProjectController();
        Project currentProject = projectController.getCurrentProject();

        int overallPeople = projectController.getPeopleCount();
        int excCount = currentProject.getCountForGrade(5);
        int goodCount = currentProject.getCountForGrade(4);
        int normCount = currentProject.getCountForGrade(3);
        int badCount = currentProject.getCountForGrade(2);
        int enoughCount = excCount + goodCount + normCount;

        allNumber.setText(overallPeople + " մարդ");

        if (overallPeople == 0) {
            overallPeople = 1;
            //lazy hack :)))
        }
        float excPercent = excCount / ((float) overallPeople) * 100f;
        excellent.setText( excCount + ", " + String.format("%.2f", excPercent) + "%");

        float goodPercent = goodCount / ((float) overallPeople) * 100f;
        good.setText( goodCount + ", " + String.format("%.2f", goodPercent) + "%");

        float normPercent = normCount / ((float) overallPeople) * 100f;
        ok.setText( normCount + ", " + String.format("%.2f", normPercent) + "%");

        float badPercent = badCount / ((float) overallPeople) * 100f;
        bad.setText( badCount + ", " + String.format("%.2f", badPercent) + "%");

        // TODO: 4/29/2022 make calculation
        getFreed.setText("0 մարդ");

        float overallOkPercent = enoughCount / ((float) overallPeople) * 100f;
        overallOks.setText( enoughCount + ", " + String.format("%.2f", overallOkPercent) + "%");
    }

}
