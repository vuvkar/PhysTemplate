package com.phys.template.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.*;
import com.phys.template.PhysTemplate;
import com.phys.template.UIStage;

public class MainMenu extends Table {

    UIStage stage;
    private MenuItem saveProject;
    private MenuItem export;
    private PopupMenu openRecentPopup;

    public MainMenu(UIStage stage) {
        setSkin(stage.getSkin());
        this.stage = stage;

//        setBackground(stage.getSkin().getDrawable("button-main-menu"));
    }

    public void build() {
        clearChildren();

        MenuBar menuBar = new MenuBar();
        Menu projectMenu = new Menu("File");
        menuBar.addMenu(projectMenu);

        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");
        helpMenu.addItem(about);
        menuBar.addMenu(helpMenu);

        about.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                VisDialog dialog = Dialogs.showOKDialog(stage.getStage(), "About Talos 1.4.1", "Talos is a an open source node based FX and Shader editor");
            }
        });

        final MenuItem newProject = new MenuItem("New TalosProject", icon("icon-file-text"));
        final MenuItem openProject = new MenuItem("Open TalosProject", icon("icon-folder"));
        MenuItem openRecent = new MenuItem("Open Recent", icon("icon-folder-star"));
        saveProject = new MenuItem("Save", icon("icon-star"));
        export = new MenuItem("Export", icon("icon-star"));

        openRecentPopup = new PopupMenu();
        openRecent.setSubMenu(openRecentPopup);

        MenuItem settings = new MenuItem("Preferences");

        projectMenu.addItem(newProject);
        projectMenu.addItem(openProject);
        projectMenu.addItem(openRecent);
        projectMenu.addItem(saveProject);
        projectMenu.addItem(export);
        projectMenu.addSeparator();
        projectMenu.addSeparator();
        projectMenu.addItem(settings);
        projectMenu.addSeparator();

        newProject.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.newProjectAction();
            }
        });

        openProject.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.openProjectAction();
            }
        });

        saveProject.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (!saveProject.isDisabled()) stage.saveProjectAction();
            }
        });

        export.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.exportAction();
            }
        });

        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.getStage().addActor(stage.settingsDialog.fadeIn());
            }
        });

        add(menuBar.getTable()).left().grow();


        // adding key listeners for menu items
        stage.getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.N && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    if (!newProject.isDisabled()) {
                        PhysTemplate.Instance().ProjectController().newProject();
                    }
                }
                if (keycode == Input.Keys.O && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    if (!openProject.isDisabled()) {
                        stage.openProjectAction();
                    }
                }
                if (keycode == Input.Keys.S && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    if (!saveProject.isDisabled()) {
                        stage.saveProjectAction();
                    }
                }
                if (keycode == Input.Keys.E && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                    if (!saveProject.isDisabled()) {
                        stage.exportAction();
                    }
                }

                return super.keyDown(event, keycode);
            }
        });

        PhysTemplate.Instance().ProjectController().updateRecentsList();
    }


    public void disableItem(MenuItem item) {
        item.setDisabled(true);
    }

    public void enableItem(MenuItem item) {
        item.setDisabled(false);
    }

    public void updateRecentsList(Array<String> list) {
        openRecentPopup.clear();

        for (String path : list) {
            final FileHandle handle = Gdx.files.absolute(path);
            if (!handle.exists()) continue;
            ;
            String name = handle.name();
            MenuItem item = new MenuItem(name);
            item.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    PhysTemplate.Instance().ProjectController().loadProject(handle);
                }
            });
            openRecentPopup.addItem(item);
        }
    }

    private Image icon(String name) {
        return new Image(getSkin().getDrawable(name));
    }
}
