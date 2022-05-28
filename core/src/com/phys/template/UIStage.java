package com.phys.template;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.ConfirmDialogListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.phys.template.models.Metadata;
import com.phys.template.models.Person;
import com.phys.template.input.PlatformSpecificManager;
import com.phys.template.views.StatisticsWidget;
import com.phys.template.views.exerciseWidgets.AddExercisePopup;
import com.phys.template.views.exerciseWidgets.ExercisesGroupWidget;
import com.phys.template.views.MainMenu;
import com.phys.template.views.metaWidgets.MetaInfoGroupWidget;
import com.phys.template.views.peopleWidgets.AddRestrictionPopup;
import com.phys.template.views.peopleWidgets.EditPersonPopup;
import com.phys.template.views.peopleWidgets.PeopleListWidget;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.*;

public class UIStage {

    public static int TOP_PART_HEIGHT = 82;

    private final Stage stage;
    private final Skin skin;

    // TODO: 11/19/2021 Handle project drag and drop
    private final DragAndDrop dragAndDrop;

    private Table fullScreenTable;
    private MetaInfoGroupWidget metaInfoGroupWidget;
    private ExercisesGroupWidget exercisesGroupWidget;
    private StatisticsWidget statisticsWidget;
    private PeopleListWidget peopleListWidget;
    private AddExercisePopup addExercisePopup;
    private AddRestrictionPopup addRestrictionPopup;
    private EditPersonPopup addPersonPopup;
    private Table bottomButtonTable;

    PlatformSpecificManager platformSpecificManager;

    private MainMenu mainMenu;

    public UIStage(Skin skin, PlatformSpecificManager platformSpecificManager) {
        PolygonSpriteBatch batch = new PolygonSpriteBatch();
        this.stage = new Stage(new FixedHeightViewport(1080, new OrthographicCamera()), batch);
        this.skin = skin;
        this.dragAndDrop = new DragAndDrop();
        this.platformSpecificManager = platformSpecificManager;

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.V && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    onPaste();
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    public void init() {
        fullScreenTable = new Table();
        fullScreenTable.setFillParent(true);
        fullScreenTable.top().left();
        fullScreenTable.defaults().pad(5);

        stage.addActor(fullScreenTable);
        constructExerciseWidget();
        constructMetaInfoListWidget();
        fullScreenTable.row();
        constructPeopleListWidget();
        constructStatisticsWidget();
        fullScreenTable.row();
        constructBottomTable();

        addExercisePopup = new AddExercisePopup();
        addRestrictionPopup = new AddRestrictionPopup();
        addPersonPopup = new EditPersonPopup();
    }

    private void constructBottomTable() {
        bottomButtonTable = new Table();

        VisTextButton saveButton = new VisTextButton("Պահպանել որպես ամփոփագիր", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveProjectAction();
            }
        });
        VisTextButton openButton = new VisTextButton("Բացել ամփոփագիր", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                openProjectAction();
            }
        });

        bottomButtonTable.left();
        bottomButtonTable.defaults().pad(10);
        int buttonHeight = 60;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            VisTextButton printButton = new VisTextButton("Պահպանել որպես Word", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    printProjectAction();
                }
            });
            bottomButtonTable.add(printButton).height(buttonHeight);
        }
        bottomButtonTable.add(saveButton).height(buttonHeight);
        bottomButtonTable.add(openButton).height(buttonHeight);
        fullScreenTable.add(bottomButtonTable).colspan(2);
    }

    private void constructMetaInfoListWidget() {
        metaInfoGroupWidget = new MetaInfoGroupWidget();
        fullScreenTable.add(metaInfoGroupWidget).growX();
    }

    private void constructPeopleListWidget() {
        peopleListWidget = new PeopleListWidget();
        fullScreenTable.add(peopleListWidget).grow();
    }

    private void constructExerciseWidget() {
        exercisesGroupWidget = new ExercisesGroupWidget();
        fullScreenTable.add(exercisesGroupWidget).growX().fillY();
    }

    private void constructStatisticsWidget() {
        statisticsWidget = new StatisticsWidget();
        fullScreenTable.add(statisticsWidget).growY().fillX();
    }

    private void constructMenu() {
        mainMenu = new MainMenu(this);
        mainMenu.build();
        fullScreenTable.add(mainMenu).growX();
    }

    public Stage getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }


    public void openProjectAction() {
        platformSpecificManager.openProject();



    }

    public MetaInfoGroupWidget getMetaWidget() {
        return metaInfoGroupWidget;
    }

    public void printProjectAction() {
        PhysTemplate.Instance().ProjectController().getCurrentProject().importMetaDatas();
        platformSpecificManager.saveWord();
    }

    public void saveProjectAction() {
        PhysTemplate.Instance().ProjectController().getCurrentProject().importMetaDatas();
        platformSpecificManager.saveProject();
    }

    public void newProjectAction() {
        // TODO: 11/19/2021 handle new project action
    }

    public void showExerciseAddPopup() {
        addExercisePopup.refreshContent();
        stage.addActor(addExercisePopup.fadeIn());
    }

    public void showPersonAddPopup() {
        addPersonPopup.updateForMode(true);
        stage.addActor(addPersonPopup.fadeIn());
    }

    public void hidePersonAddPopup() {
        addPersonPopup.fadeOut();
    }

    public void hideExerciseAddPopup() {
        addExercisePopup.fadeOut();
    }

    private void updateExerciseContent() {
        exercisesGroupWidget.updateContent();
    }

    private void updatePeopleContent() {
        peopleListWidget.updateContent();
    }

    public void updateStatistics() {
        statisticsWidget.refreshContent();
    }

    public void updateContent() {
        updateExerciseContent();
        updateTopRow();
        updatePeopleContent();
        updateStatistics();
        updateMetadata();

        updateEditPopupWindow();
    }

    private void updateMetadata() {
        metaInfoGroupWidget.refreshContent();
    }

    private void updateEditPopupWindow() {
        if (addPersonPopup.hasParent()) {
            addPersonPopup.refreshRestrictionContent();
            // TODO: 5/13/2022 update coloring for restriction addition and deleteion
        }
    }

    public void showEditPersonPopup(Person person) {
        addPersonPopup.updateForMode(false);
        addPersonPopup.updateFor(person);
        stage.addActor(addPersonPopup.fadeIn());
    }

    public void onPaste() {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        if (t == null)
            return;
        try {
            // TODO: 12/21/2021 handle copy pasting
            String transferData = (String) t.getTransferData(DataFlavor.stringFlavor);
            final String[] strings = processPastedData(transferData);
            int length = strings.length;
            if (length > 0) {
                Dialogs.showConfirmDialog(stage, "Ավելացնել նոր զինծառայողներ", "Ցանկանում եք ավելացնել " + length + " զինծառայող",
                        new String[]{"Այո", "Ոչ"}, new Boolean[]{true, false}, new ConfirmDialogListener<Boolean>() {
                            @Override
                            public void result(Boolean result) {
                                if (result) {
                                    for (String string : strings) {
                                        PhysTemplate.Instance().ProjectController().createPersonByName(string);
                                    }

                                    PhysTemplate.Instance().UIStage().updateContent();
                                }
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] processPastedData(String data) {
        String[] split = data.split("\n");
        return split;
    }

    private void updateTopRow() {
        peopleListWidget.topRow.updateTopRowForExercises(PhysTemplate.Instance().ProjectController().getCurrentProjectExercises());
    }

    public void showRestrictionAddPopup(Person person) {
        addRestrictionPopup.refreshContent(person);
        stage.addActor(addRestrictionPopup.fadeIn());
    }

    public void hideRestrictionPopup() {
        addRestrictionPopup.fadeOut();
    }

    public PlatformSpecificManager<?> getPlatformSpecificManager() {
        return platformSpecificManager;
    }
}