package com.jankuester.ggj.twentyseventeen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jankuester.ggj.twentyseventeen.logic.ModelMap;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelFactory;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreviewFactory;
import com.jankuester.ggj.twentyseventeen.screens.ErrorScreen;
import com.jankuester.ggj.twentyseventeen.screens.GameScreen;
import com.jankuester.ggj.twentyseventeen.screens.LoadingScreen;
import com.jankuester.ggj.twentyseventeen.screens.Optionsscreen;
import com.jankuester.ggj.twentyseventeen.screens.PreviewScreen;
import com.jankuester.ggj.twentyseventeen.screens.ScreenBase;
import com.jankuester.ggj.twentyseventeen.screens.StartScreen;
import com.jankuester.ggj.twentyseventeen.screens.actions.ScreenMenuActions;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenFactory;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenComponentFactory;
import com.jankuester.ggj.twentyseventeen.screens.components.ScreenMenuTextButton;
import com.jankuester.ggj.twentyseventeen.system.GlobalGameSettings;

public class GGJTwentySeventeenGame extends Game {

    private StartScreen startScreen;
    private Optionsscreen optionsSreen;
    private PreviewScreen previewMapScreen;
    private PreviewScreen previewVehicleScreen;
    private LoadingScreen loadingScreen;
    private ErrorScreen errorScreen;

    private MenuListener menuListener;
    private HashMap<String, Sound> systemSounds;

    // ====================================================================
    //
    // CREATE
    //
    // ====================================================================

    @Override
    public void create() {
	try {
	    loadingScreen();
	    loadGlobalSettings();
	    createStartScreen();
	    queueScreen(startScreen);
	} catch (Exception e) {
	    errorScreen(e);
	    queueScreen(errorScreen);
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

    private void queueScreen(ScreenBase screen) {
	setScreen(loadingScreen);
	try {
	    setScreen(screen);
	} catch (Exception e) {
	    errorScreen(e);
	}
    }

    private void errorScreen(Exception e) {
	if (errorScreen != null)
	    errorScreen.dispose();
	errorScreen = ScreenFactory.createErrorScreen(e);
	queueScreen(errorScreen);
    }

    private void loadingScreen() {
	if (loadingScreen == null)
	    loadingScreen = ScreenFactory.createLoadingScreen();
	queueScreen(loadingScreen);
    }

    public void startScreen() {
	if (startScreen == null)
	    createStartScreen();
	queueScreen(startScreen);
    }
    
    public void createStartScreen() {
	startScreen = new StartScreen();
	Texture startBg = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	startScreen.setBackgroundImage(startBg, false);
	startScreen.setBackgroundAudio(Gdx.audio.newMusic(Gdx.files.internal("audio/menu/bg_main.mp3")));
	startScreen.addInputListener(menuListener);

	// create big name
	startScreen.addText(
		ScreenComponentFactory.createLabel("GLOBAL GAME JAM 2017", ScreenComponentFactory.largeFont, null));

	startScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIEW_MAPS, "ARCADE MODE", 300, 200, 0, 0));
	startScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.OPTIONS, "OPTIONS", 300, 200, 0, 0));
	startScreen.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.EXIT, "EXIT", 300, 200, 0, 0));
	startScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    public void optionsScreen() {
	if (optionsSreen == null)
	    createOptionsScreen();
	queueScreen(optionsSreen);
    }

    public void createOptionsScreen() {
	optionsSreen = new Optionsscreen();
	optionsSreen.addInputListener(menuListener);
	optionsSreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", 300, 100, 0, 0));
	optionsSreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void previewMapsScreen() {
	if (previewMapScreen == null) {
	    createPreviewMapScreen();
	}
	queueScreen(previewMapScreen);
    }

    private void createPreviewMapScreen() {
	previewMapScreen = new PreviewScreen();
	Texture background = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	previewMapScreen.setBackgroundImage(background, false);
	previewMapScreen.addInputListener(menuListener);
	previewMapScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIEW_VEHICLES, "SELECT", 300, 200, 0, 0));
	previewMapScreen
		.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.NEXT_MAP, "NEXT", 300, 200, 0, 0));
	previewMapScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIOUS_MAP, "PREVIOUS", 300, 200, 0, 0));
	previewMapScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", 300, 200, 0, 0));

	ModelPreview cityMapPreview = ModelPreviewFactory.createMapPreview(ModelMap.Map.MAP_EASY);
	
	previewMapScreen.addPreviewModel(cityMapPreview);
    }

    private void previewVehiclesScreen() {
	if (previewVehicleScreen == null) {
	    createPreviewVehicleScreen();
	}
	queueScreen(previewVehicleScreen);
    }

    private void createPreviewVehicleScreen() {
	previewVehicleScreen = new PreviewScreen();
	Texture background = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	previewVehicleScreen.setBackgroundImage(background, false);
	previewVehicleScreen.addInputListener(menuListener);
	previewVehicleScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.GAME, "START RACE", 300, 100, 0, 0));
	previewVehicleScreen.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIEW_MAPS,
		"BACK TO MAP PREVIEW", 300, 100, 0, 0));

	ModelPreview vehiclePreview_mid = ModelPreviewFactory.createVehiclePreview(ModelMap.Vehicle.VEHICLE_MIDDLEWEIGHT);
	vehiclePreview_mid.setScale(4);
	previewVehicleScreen.addPreviewModel(vehiclePreview_mid);
    }

    private void gameScreen() {

	GameScreen gameScreen = new GameScreen();
	gameScreen.addInputListener(menuListener);

	// get selection from the other screens
	String selectedMap = previewMapScreen.getCurrentPreview().getId();

	queueScreen(gameScreen);
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
	    startScreen();
	    break;
	case ScreenMenuActions.OPTIONS:
	    optionsScreen();
	    break;

	case ScreenMenuActions.PREVIEW_MAPS:
	    previewMapsScreen();
	    break;
	    
	//TODO move to prev screen as internal function
	case ScreenMenuActions.NEXT_MAP:
	    previewMapScreen.next();
	    break;
	case ScreenMenuActions.PREVIOUS_MAP:
	    previewMapScreen.previous();
	    break;

	case ScreenMenuActions.PREVIEW_VEHICLES:
	    previewVehiclesScreen();
	    break;

	case ScreenMenuActions.GAME:
	    gameScreen();
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
