package com.phys.template;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.phys.template.controllers.DataController;
import com.phys.template.controllers.DocumentController;
import com.phys.template.controllers.ProjectController;

public class PhysTemplate extends ApplicationAdapter {

	private Preferences preferences;

	private Skin skin;

	private UIStage uiStage;
	private ProjectController projectController;
	private DataController dataController;
	private DocumentController documentController;
	private InputMultiplexer inputMultiplexer;

	private static PhysTemplate instance;

	@Override
	public void create () {

		PhysTemplate.instance = this;

		preferences = Gdx.app.getPreferences("phys-template-preferences");

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		skin.addRegions(atlas);

		VisUI.load(skin);

		uiStage = new UIStage(skin);

		dataController = new DataController();

		projectController = new ProjectController();
		projectController.newProject();
		projectController.init();

		documentController = new DocumentController();

		uiStage.init();

		inputMultiplexer = new InputMultiplexer(uiStage.getStage());

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public static PhysTemplate Instance() {
		return instance;
	}

	public UIStage UIStage() {
		return uiStage;
	}

	public ProjectController ProjectController () {
		return projectController;
	}

	public DataController DataController() {
		return dataController;
	}

	public DocumentController DocumentController() {
		return documentController;
	}


	@Override
	public void render () {
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

		uiStage.getStage().act();
		uiStage.getStage().draw();
	}
	
	@Override
	public void dispose () {
		uiStage.getStage().dispose();
	}
}
