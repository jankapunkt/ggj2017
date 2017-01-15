package com.jankuester.ggj.twentyseventeen;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jankuester.ggj.twentyseventeen.logic.ModelMap;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelFactory;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;
import com.jankuester.ggj.twentyseventeen.screens.GameScreen;
import com.jankuester.ggj.twentyseventeen.screens.Optionsscreen;
import com.jankuester.ggj.twentyseventeen.screens.PreviewScreen;
import com.jankuester.ggj.twentyseventeen.screens.StartScreen;
import com.jankuester.ggj.twentyseventeen.screens.actions.ScreenMenuActions;
import com.jankuester.ggj.twentyseventeen.screens.components.UIFactory;
import com.jankuester.ggj.twentyseventeen.screens.components.ScreenMenuTextButton;
import com.jankuester.ggj.twentyseventeen.system.GlobalGameSettings;

public class GGJTwentySeventeenGame extends Game {

    private StartScreen startScreen;
    private Optionsscreen optionsSreen;

    private MenuListener menuListener;
    private HashMap<String, Sound> systemSounds;
    private PreviewScreen previewMapScreen;
    private PreviewScreen previewVehicleScreen;

    // ====================================================================
    //
    // CREATE
    //
    // ====================================================================

    @Override
    public void create() {
	try {
	    loadGlobalSettings();
	    createStartScreen();
	    createOptionsScreen();
	    setScreen(startScreen);
	} catch (Exception e) {
	    // TODO: show error screen with error report
	    System.out.println(e.getMessage());
	    e.printStackTrace();
	}
    }

    private void loadGlobalSettings() throws IOException {
	Gdx.graphics.setVSync(true);
	Gdx.graphics.setResizable(true);

	ModelMap.loadFromProperties("models/modelmapping.properties");
	menuListener = new MenuListener(this);
	initSystemSounds();
    }

    private void initSystemSounds() {
	systemSounds = new HashMap<String, Sound>();
	systemSounds.put("menuAction", Gdx.audio.newSound(Gdx.files.internal("audio/menu/enter.mp3")));
	systemSounds.put("menuBack", Gdx.audio.newSound(Gdx.files.internal("audio/menu/back.mp3")));
	systemSounds.put("menuForbidden", Gdx.audio.newSound(Gdx.files.internal("audio/menu/forbidden.mp3")));
	systemSounds.put("menuHover", Gdx.audio.newSound(Gdx.files.internal("audio/menu/hover.mp3")));
    }

    public void createStartScreen() {
	startScreen = new StartScreen();
	Texture startBg = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	startScreen.setBackgroundImage(startBg, false);
	startScreen.setBackgroundAudio(Gdx.audio.newMusic(Gdx.files.internal("audio/menu/bg_main.mp3")));
	startScreen.addInputListener(menuListener);

	// create big name
	startScreen.addText(UIFactory.createLabel("GLOBAL GAME JAM 2017", UIFactory.largeFont, null));

	startScreen
		.addButton(UIFactory.createMenuButton(ScreenMenuActions.PREVIEW_MAPS, "ARCADE MODE", 300, 200, 0, 0));
	startScreen.addButton(UIFactory.createMenuButton(ScreenMenuActions.OPTIONS, "OPTIONS", 300, 200, 0, 0));
	startScreen.addButton(UIFactory.createMenuButton(ScreenMenuActions.EXIT, "EXIT", 300, 200, 0, 0));
	startScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void createOptionsScreen() {
	optionsSreen = new Optionsscreen();
	optionsSreen.addInputListener(menuListener);
	optionsSreen
		.addButton(UIFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", 300, 100, 0, 0));
	optionsSreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void previewMaps() {
	if (previewMapScreen == null) {
	    initPreviewMapScreen();
	}
	setScreen(previewMapScreen);
    }

    private void initPreviewMapScreen() {
	previewMapScreen = new PreviewScreen();
	Texture background = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	previewMapScreen.setBackgroundImage(background, false);
	previewMapScreen.addInputListener(menuListener);
	previewMapScreen.addButton(UIFactory.createMenuButton(ScreenMenuActions.PREVIEW_VEHICLES, "SELECT", 300, 200, 0, 0));
	previewMapScreen.addButton(UIFactory.createMenuButton(ScreenMenuActions.NEXT_MAP, "NEXT", 300, 200, 0, 0));
	previewMapScreen.addButton(UIFactory.createMenuButton(ScreenMenuActions.PREVIOUS_MAP, "PREVIOUS", 300, 200, 0, 0));
	previewMapScreen.addButton(UIFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", 300, 200, 0, 0));

	ModelPreview cityMapPreview = new ModelPreview(
		ModelMap.getName(ModelMap.Map.MAP_EASY),
		ModelMap.getDescription(ModelMap.Map.MAP_EASY), 
		ModelMap.getDifficulty(ModelMap.Map.MAP_EASY),
		ModelFactory.getGameModelInstance(ModelMap.getPreviewPath(ModelMap.Map.MAP_EASY), 0, 0, 0),
		ModelMap.getId(ModelMap.Map.MAP_EASY));
	previewMapScreen.addPreviewModel(cityMapPreview);
    }

    private void previewVehicles() {
	if (previewVehicleScreen == null) {
	    initPreviewVehicleScreen();
	}
	setScreen(previewVehicleScreen);
    }

    private void initPreviewVehicleScreen() {
	previewVehicleScreen = new PreviewScreen();
	Texture background = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	previewVehicleScreen.setBackgroundImage(background, false);
	previewVehicleScreen.addInputListener(menuListener);
	previewVehicleScreen
		.addButton(UIFactory.createMenuButton(ScreenMenuActions.GAME, "START RACE", 300, 100, 0, 0));
	previewVehicleScreen.addButton(
		UIFactory.createMenuButton(ScreenMenuActions.PREVIEW_MAPS, "BACK TO MAP PREVIEW", 300, 100, 0, 0));

	ModelPreview vehiclePreview_mid = new ModelPreview("Vehicle Mid", "Average", 0,
		ModelFactory.getGameModelInstance("models/vehicles/vehicle_mide.g3db", 0, 0, 0),
		"0");
	vehiclePreview_mid.setScale(4);
	previewVehicleScreen.addPreviewModel(vehiclePreview_mid);
    }

    private void startGame() {

	GameScreen gameScreen = new GameScreen();
	gameScreen.addInputListener(menuListener);

	// get selection from the other screens
	String selectedMap = previewMapScreen.getCurrentPreview().getSelectionId();

	setScreen(gameScreen);
	unloadMenuScreens();
    }

    private void unloadMenuScreens() {
	startScreen.dispose();
	startScreen = null;

	optionsSreen.dispose();
	optionsSreen = null;

	previewMapScreen.dispose();
	previewMapScreen = null;

	previewVehicleScreen.dispose();
	previewVehicleScreen = null;
    }

    // ====================================================================
    //
    // MENU ACTIONS
    //
    // ====================================================================

    private int lastMenuAction = -1;
    private int lastMenuHover = -1;

    public void onMenuAction(int action) {

	if (action > lastMenuAction) { // play menu action sound
	    systemSounds.get("menuAction").play(GlobalGameSettings.loudeness_fx);
	}
	if (action < lastMenuAction) {
	    systemSounds.get("menuBack").play(GlobalGameSettings.loudeness_fx);
	}
	lastMenuAction = action;

	switch (action) {
	case ScreenMenuActions.START:
	    setScreen(startScreen);
	    break;
	case ScreenMenuActions.OPTIONS:
	    setScreen(optionsSreen);
	    break;

	case ScreenMenuActions.PREVIEW_MAPS:
	    previewMaps();
	    break;
	case ScreenMenuActions.NEXT_MAP:
	    previewMapScreen.next();
	    break;
	case ScreenMenuActions.PREVIOUS_MAP:
	    previewMapScreen.previous();
	    break;

	case ScreenMenuActions.PREVIEW_VEHICLES:
	    previewVehicles();
	    break;

	case ScreenMenuActions.GAME:
	    startGame();
	    break;

	case ScreenMenuActions.EXIT:
	    Gdx.app.exit();
	    break;
	case ScreenMenuActions.NULL:
	default:
	    System.out.println("Menu action: null - This should not happen");
	    break;
	}
    }

    public void onMenuHover(int action) {
	if (action != lastMenuHover) {
	    systemSounds.get("menuHover").play();
	}
	lastMenuHover = action;
    }

    // ====================================================================
    //
    // MENU LISTENER
    //
    // ====================================================================

    class MenuListener extends InputListener {
	private GGJTwentySeventeenGame game;

	public MenuListener(GGJTwentySeventeenGame game) {
	    super();
	    this.game = game;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
	    if (event.getListenerActor() instanceof ScreenMenuTextButton) {
		ScreenMenuTextButton target = (ScreenMenuTextButton) event.getListenerActor();
		game.onMenuAction(target.getAction());
	    } else {
		lastMenuHover = -1;
	    }
	    return super.touchDown(event, x, y, pointer, button);
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
	    if (event.getListenerActor() instanceof ScreenMenuTextButton) {
		ScreenMenuTextButton target = (ScreenMenuTextButton) event.getListenerActor();
		game.onMenuHover(target.getAction());
	    } else {

	    }
	    return super.mouseMoved(event, x, y);
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
	    if (keycode == Input.Keys.ESCAPE) {

	    }
	    return super.keyDown(event, keycode);
	}
    }
}
