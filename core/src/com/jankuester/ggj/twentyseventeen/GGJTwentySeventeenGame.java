package com.jankuester.ggj.twentyseventeen;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jankuester.ggj.twentyseventeen.screens.GameScreen;
import com.jankuester.ggj.twentyseventeen.screens.Optionsscreen;
import com.jankuester.ggj.twentyseventeen.screens.StartScreen;
import com.jankuester.ggj.twentyseventeen.screens.actions.ScreenMenuActions;
import com.jankuester.ggj.twentyseventeen.screens.components.GameMenuTextButton;
import com.jankuester.ggj.twentyseventeen.screens.components.ButtonFactory;


public class GGJTwentySeventeenGame extends Game {

    private StartScreen startScreen;
    private Optionsscreen optionsSreen;
    
    private MenuListener menuListener;
    private HashMap<String, Music> systemSounds;

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
	menuListener = new MenuListener(this);
	ButtonFactory.init();
	initSystemSounds();
    }

    private void initSystemSounds() {
	systemSounds = new HashMap<String, Music>();
	systemSounds.put("menuAction", Gdx.audio.newMusic(Gdx.files.internal("audio/menu/enter.mp3")));
	systemSounds.put("menuBack", Gdx.audio.newMusic(Gdx.files.internal("audio/menu/back.mp3")));
	systemSounds.put("menuForbidden", Gdx.audio.newMusic(Gdx.files.internal("audio/menu/forbidden.mp3")));
	systemSounds.put("menuHover", Gdx.audio.newMusic(Gdx.files.internal("audio/menu/hover.mp3")));
    }

    public void createStartScreen() {
	startScreen = new StartScreen();
	startScreen.addInputListener(menuListener);
	startScreen.addButton(ButtonFactory.createMenuButton(ScreenMenuActions.GAME, "START GAME", Color.GREEN));
	startScreen.addButton(ButtonFactory.createMenuButton(ScreenMenuActions.OPTIONS, "OPTIONS", Color.BLACK));
	startScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void createOptionsScreen() {
	optionsSreen = new Optionsscreen();
	optionsSreen.addInputListener(menuListener);
	optionsSreen.addButton(ButtonFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", Color.BLACK));
	optionsSreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
	    systemSounds.get("menuAction").play();
	}
	if (action < lastMenuAction) {
	    systemSounds.get("menuBack").play();
	}
	lastMenuAction = action;

	switch (action) {
	case ScreenMenuActions.START:
	    setScreen(startScreen);
	    break;
	case ScreenMenuActions.OPTIONS:
	    setScreen(optionsSreen);
	    break;
	case ScreenMenuActions.GAME:
	    startGame();
	    break;
	case ScreenMenuActions.NULL:
	default:
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
	    if (event.getListenerActor() instanceof GameMenuTextButton) {
		GameMenuTextButton target = (GameMenuTextButton) event.getListenerActor();
		game.onMenuAction(target.getAction());
	    } else {
		lastMenuHover = -1;
	    }
	    return super.touchDown(event, x, y, pointer, button);
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
	    if (event.getListenerActor() instanceof GameMenuTextButton) {
		GameMenuTextButton target = (GameMenuTextButton) event.getListenerActor();
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
