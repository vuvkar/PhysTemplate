package com.phys.template;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.phys.template.controllers.ProjectController;
import com.phys.template.models.Person;
import com.phys.template.views.StatisticsWidget;
import com.phys.template.views.exerciseWidgets.AddExercisePopup;
import com.phys.template.views.exerciseWidgets.ExercisesGroupWidget;
import com.phys.template.views.MainMenu;
import com.phys.template.views.metaWidgets.MetaInfoGroupWidget;
import com.phys.template.views.peopleWidgets.EditPersonPopup;
import com.phys.template.views.peopleWidgets.PeopleListWidget;
import com.phys.template.views.dialogs.SettingsDialog;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileFilter;

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
    private EditPersonPopup addPersonPopup;
    private Table bottomButtonTable;

    // TODO: 11/19/2021 Handle file chooser
    FileChooser fileChooser;
    // TODO: 11/19/2021 Handle settings dialog
    public SettingsDialog settingsDialog;

    private MainMenu mainMenu;

    public UIStage(Skin skin) {
        this.stage = new Stage(new ScreenViewport(), new PolygonSpriteBatch());
        this.skin = skin;
        this.dragAndDrop = new DragAndDrop();

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

//        constructMenu();
//        fullScreenTable.row();

//        fullScreenTable.row();
        constructExerciseWidget();
        constructMetaInfoListWidget();
        fullScreenTable.row();
        constructPeopleListWidget();
        constructStatisticsWidget();
        fullScreenTable.row();
        constructBottomTable();

        initFileChoosers();

        settingsDialog = new SettingsDialog();
        addExercisePopup = new AddExercisePopup();
        addPersonPopup = new EditPersonPopup();
    }

    private void constructBottomTable() {
        bottomButtonTable = new Table();
        VisTextButton printButton = new VisTextButton("Պահպանել որպես Word", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                printProjectAction();
            }
        });

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
        bottomButtonTable.add(printButton);
        bottomButtonTable.add(saveButton);
        bottomButtonTable.add(openButton);
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
        fullScreenTable.add(exercisesGroupWidget).growX();
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

    private void initFileChoosers() {
        fileChooser = new FileChooser(FileChooser.Mode.SAVE);
    }

    public void openProjectAction() {
        fileChooser.setMode(FileChooser.Mode.OPEN);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getAbsolutePath().endsWith("fpe");
            }
        });
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                String path = file.first().file().getAbsolutePath();
                PhysTemplate.Instance().ProjectController().loadProject(Gdx.files.absolute(path));
            }
        });

        stage.addActor(fileChooser.fadeIn());
    }

    public MetaInfoGroupWidget getMetaWidget() {
        return metaInfoGroupWidget;
    }

    public void printProjectAction() {
        final String ext = ".docx";

        fileChooser.setMode(FileChooser.Mode.SAVE);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getAbsolutePath().endsWith(ext);
            }
        });
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                String path = file.first().file().getAbsolutePath();
                if (!path.endsWith(ext)) {
                    if (path.indexOf(".") > 0) {
                        path = path.substring(0, path.indexOf("."));
                    }
                    path += ext;
                }
                FileHandle handle = Gdx.files.absolute(path);
                try {

                    PhysTemplate.Instance().DocumentController().createDocumentForProject(PhysTemplate.Instance().ProjectController().getCurrentProject(), handle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fileChooser.setName("");

        stage.addActor(fileChooser.fadeIn());
    }

    public void saveProjectAction() {
        final String ext = ".fpe";
        fileChooser.setMode(FileChooser.Mode.SAVE);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getAbsolutePath().endsWith(ext);
            }
        });
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                String path = file.first().file().getAbsolutePath();
                if (!path.endsWith(ext)) {
                    if (path.indexOf(".") > 0) {
                        path = path.substring(0, path.indexOf("."));
                    }
                    path += ext;
                }
                FileHandle handle = Gdx.files.absolute(path);
                // TODO: 1/6/2022 save on a file
                PhysTemplate.Instance().ProjectController().saveProject(handle);
            }
        });

        fileChooser.setName("a");

        stage.addActor(fileChooser.fadeIn());
    }

    public void newProjectAction() {
        // TODO: 11/19/2021 handle new project action
    }

    public void showExerciseAddPopup() {
        addExercisePopup.refreshContent();
        stage.addActor(addExercisePopup.fadeIn());
    }

    public void showPersonAddPopup() {
        addPersonPopup.refreshExerciseContent(PhysTemplate.Instance().ProjectController().getCurrentProjectExercises());
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
    }

    public void showEditPersonPopup(Person person) {
        addPersonPopup.refreshExerciseContent(PhysTemplate.Instance().ProjectController().getCurrentProjectExercises());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTopRow() {
        peopleListWidget.topRow.updateTopRowForExercises(PhysTemplate.Instance().ProjectController().getCurrentProjectExercises());
    }
}