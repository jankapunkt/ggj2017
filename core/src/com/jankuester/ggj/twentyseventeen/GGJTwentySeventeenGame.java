package com.jankuester.ggj.twentyseventeen;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelFactory;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;
import com.jankuester.ggj.twentyseventeen.screens.GameScreen;
import com.jankuester.ggj.twentyseventeen.screens.Optionsscreen;
import com.jankuester.ggj.twentyseventeen.screens.PreviewScreen;
import com.jankuester.ggj.twentyseventeen.screens.StartScreen;
import com.jankuester.ggj.twentyseventeen.screens.actions.ScreenMenuActions;
import com.jankuester.ggj.twentyseventeen.screens.components.ScreenComponentFactory;
import com.jankuester.ggj.twentyseventeen.screens.components.ScreenMenuTextButton;
import com.jankuester.ggj.twentyseventeen.system.GlobalGameSettings;

public class GGJTwentySeventeenGame extends Game {

    private StartScreen startScreen;
    private Optionsscreen optionsSreen;

    private MenuListener menuListener;
    private HashMap<String, Sound> systemSounds;
    private PreviewScreen previewScreen;

    // ====================================================================
    //
    // CREATE
    //
    // ====================================================================

    @Override
    public void create() {
	loadGlobalSettings();
	createStartScreen();
	createOptionsScreen();
	setScreen(startScreen);
    }

    private void loadGlobalSettings() {
	Gdx.graphics.setVSync(true);
	Gdx.graphics.setResizable(true);

	menuListener = new MenuListener(this);
	ScreenComponentFactory.init();
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
	startScreen.addText(ScreenComponentFactory.createLabel("GLOBAL GAME JAM 2017"));
	startScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIEW_MAPS, "ARCADE MODE", Color.GREEN));
	startScreen
		.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.OPTIONS, "OPTIONS", Color.BLACK));
	startScreen.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.EXIT, "EXIT", Color.BLACK));
	startScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void createOptionsScreen() {
	optionsSreen = new Optionsscreen();
	optionsSreen.addInputListener(menuListener);
	optionsSreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", Color.BLACK));
	optionsSreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void previewMaps() {
	if (previewScreen == null) {
	    initPreviewScreen();
	}
	setScreen(previewScreen);
    }

    private void initPreviewScreen() {
	previewScreen = new PreviewScreen();
	Texture background = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	previewScreen.setBackgroundImage(background, false);
	previewScreen.addInputListener(menuListener);
	previewScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIEW_VEHICLES, "SELECT", Color.BLACK));
	previewScreen
		.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.NEXT_MAP, "NEXT", Color.BLACK));
	previewScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIOUS_MAP, "PREVIOUS", Color.BLACK));
	previewScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", Color.BLACK));

	ModelPreview cityMapPreview = new ModelPreview("City Map", "crusin down tha street", 0,
		ModelFactory.getGameModelInstance("models/maps/previews/preview_city.g3db", 0, 0, 0));
	previewScreen.addPreviewModel(cityMapPreview);
	
	previewScreen.getMenuTable().setBackground();
    }

    private void previewVehicles() {

    }

    private void startGame() {
	GameScreen gameScreen = new GameScreen();
	gameScreen.addInputListener(menuListener);
	setScreen(gameScreen);
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
	    previewScreen.next();
	    break;
	case ScreenMenuActions.PREVIOUS_MAP:
	    previewScreen.previous();
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
