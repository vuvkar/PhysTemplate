package com.phys.template.views;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntArray;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.phys.template.PhysTemplate;
import com.phys.template.controllers.ProjectController;
import com.phys.template.models.Grade;
import com.phys.template.models.Project;


public class StatisticsWidget extends Table {

    private Table mainContainer;
    private VisLabel headerLabel;

    private VisLabel allNumberLabel = new VisLabel();
    private VisLabel excellent = new VisLabel();
    private VisLabel good = new VisLabel();
    private VisLabel ok = new VisLabel();
    private VisLabel bad = new VisLabel();
    private VisLabel overallOks = new VisLabel();
    private VisLabel getFreed = new VisLabel();
    private VisLabel finalGradeLabel = new VisLabel();

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

        mainContainer.add(new VisLabel("Ստուգվել է"));
        mainContainer.add(allNumberLabel);
        mainContainer.row();

        mainContainer.add(new VisLabel("Գերազանց"));
        mainContainer.add(excellent);
        mainContainer.row();

        mainContainer.add(new VisLabel("Լավ"));
        mainContainer.add(good);
        mainContainer.row();

        mainContainer.add(new VisLabel("Բավարար"));
        mainContainer.add(ok);
        mainContainer.row();

        mainContainer.add(new VisLabel("Անբավարար"));
        mainContainer.add(bad);
        mainContainer.row();

        mainContainer.add(new VisLabel("Դրական են \nստացել"));
        mainContainer.add(overallOks);
        mainContainer.row();

        mainContainer.add(new VisLabel("Ազատվել են"));
        mainContainer.add(getFreed);
        mainContainer.row();

        mainContainer.add(new VisLabel("Ընդհանուր \nգնահատական"));
        mainContainer.add(finalGradeLabel);
    }

    public void refreshContent() {
        ProjectController projectController = PhysTemplate.Instance().ProjectController();
        Project currentProject = projectController.getCurrentProject();

        int overallPeople = projectController.getPeopleCount();
        int excCount = currentProject.getCountForGrade(Grade.EXCELLENT);
        int goodCount = currentProject.getCountForGrade(Grade.GOOD);
        int normCount = currentProject.getCountForGrade(Grade.OK);
        int badCount = currentProject.getCountForGrade(Grade.BAD);
        int enoughCount = excCount + goodCount + normCount;
        Grade finalGrade = currentProject.getFinalGrade();

        allNumberLabel.setText(overallPeople + " մարդ");

        if (overallPeople == 0) {
            overallPeople = 1;
            //lazy hack :)))
        }
        excellent.setText( excCount + ", " + currentProject.getPercentForGrade(Grade.EXCELLENT) + "%");
        good.setText( goodCount + ", " + currentProject.getPercentForGrade(Grade.GOOD) + "%");
        ok.setText( normCount + ", " + currentProject.getPercentForGrade(Grade.OK) + "%");
        bad.setText( badCount + ", " + currentProject.getPercentForGrade(Grade.BAD) + "%");

        // TODO: 4/29/2022 make calculation
        getFreed.setText("0 մարդ");

        IntArray availableExercises = new IntArray();
        availableExercises.insertRange(0, 7);


        overallOks.setText( enoughCount + ", " + currentProject.getNormalPercent() + "%");

        finalGradeLabel.setText(finalGrade.getNumericalGrade() + " " + finalGrade.getDescription(false, true));
    }

}
