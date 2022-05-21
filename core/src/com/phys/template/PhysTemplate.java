package com.phys.template;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
		FileHandle internal = Gdx.files.internal("skin/uiskin.json");
		skin = new Skin();
		skin.addRegions(atlas);
		generateFonts(skin);
		skin.load(internal);

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

	private void generateFonts(Skin skin) {
		SmartFontGenerator fontGen = new SmartFontGenerator();
		FileHandle exoFile = Gdx.files.internal("GHEAGrpalatReg.otf");

		int bigFontSize = 35;
		BitmapFont fontLarge = fontGen.createFont(exoFile, "grapalat_25", bigFontSize);
		int mediumFontSize = 30;
		BitmapFont fontMedium = fontGen.createFont(exoFile, "grapalat_20", mediumFontSize );
		int smallFontSize = 25;
		BitmapFont fontSmall = fontGen.createFont(exoFile, "grapalat_15", smallFontSize );

		skin.add("grapalat_25", fontLarge);
		skin.add("grapalat_20", fontMedium);
		skin.add("grapalat_15", fontSmall);
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
