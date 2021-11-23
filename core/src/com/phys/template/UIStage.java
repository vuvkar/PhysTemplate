package com.phys.template;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.phys.template.models.Person;
import com.phys.template.views.exerciseWidgets.AddExercisePopup;
import com.phys.template.views.exerciseWidgets.ExercisesGroupWidget;
import com.phys.template.views.MainMenu;
import com.phys.template.views.peopleWidgets.EditPersonPopup;
import com.phys.template.views.peopleWidgets.PeopleListWidget;
import com.phys.template.views.dialogs.SettingsDialog;

public class UIStage {

    private final Stage stage;
    private final Skin skin;

    // TODO: 11/19/2021 Handle project drag and drop
    private final DragAndDrop dragAndDrop;

    private Table fullScreenTable;
    private ExercisesGroupWidget exercisesGroupWidget;
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
    }

    public void init() {
        fullScreenTable = new Table();
        fullScreenTable.setFillParent(true);
        fullScreenTable.top().left();
        fullScreenTable.defaults().pad(5);

        stage.addActor(fullScreenTable);

//        constructMenu();
//        fullScreenTable.row();
        constructExerciseWidget();
        fullScreenTable.row();
        constructPeopleListWidget();
        fullScreenTable.row();
        constructBottomTable();

        initFileChoosers();

        settingsDialog = new SettingsDialog();
        addExercisePopup = new AddExercisePopup();
        addPersonPopup = new EditPersonPopup();

        // TODO: 11/19/2021 Handle exercise loading from file
//        FileHandle list = Gdx.files.internal("modules.xml");
//        XmlReader xmlReader = new XmlReader();
//        XmlReader.Element root = xmlReader.parse(list);
//        moduleListPopup = new ModuleListPopup(root);
    }

    private void constructBottomTable() {
        bottomButtonTable = new Table();
        VisTextButton printButton = new VisTextButton("Տպել", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: 11/19/2021 handle export same as print
            }
        });
        VisTextButton saveButton = new VisTextButton("Պահպանել", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: 11/19/2021 handle project save
            }
        });
        bottomButtonTable.left();
        bottomButtonTable.defaults().pad(10);
        bottomButtonTable.add(printButton);
        bottomButtonTable.add(saveButton);
        fullScreenTable.add(bottomButtonTable);
    }

    private void constructPeopleListWidget() {
        peopleListWidget = new PeopleListWidget();
        fullScreenTable.add(peopleListWidget).grow();
    }

    private void constructExerciseWidget() {
        exercisesGroupWidget = new ExercisesGroupWidget();
        fullScreenTable.add(exercisesGroupWidget).growX();
    }

    private void constructMenu () {
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
//        fileChooser = new FileChooser(FileChooser.Mode.SAVE);
//        fileChooser.setBackground(skin.getDrawable("window-noborder"));
    }

    public void openProjectAction() {
        // TODO: 11/19/2021 handle open project action
    }

    public void saveProjectAction() {
        // TODO: 11/19/2021 handle save project action
    }

    public void exportAction() {
        // TODO: 11/19/2021 handle export project action same as print function
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

    public void updateExerciseContent() {
        exercisesGroupWidget.updateContent();
        peopleListWidget.updateContent();
    }

    public void updatePeopleContent() {
        peopleListWidget.updateContent();
    }

    public void showEditPersonPopup(Person person) {
        addPersonPopup.refreshExerciseContent(PhysTemplate.Instance().ProjectController().getCurrentProjectExercises());
        addPersonPopup.updateForMode(false);
        addPersonPopup.updateFor(person);
        stage.addActor(addPersonPopup.fadeIn());
    }
}