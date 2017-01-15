package com.jankuester.ggj.twentyseventeen;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.jankuester.ggj.twentyseventeen.logic.ModelMap;
import com.jankuester.ggj.twentyseventeen.screens.ErrorScreen;
import com.jankuester.ggj.twentyseventeen.screens.GameScreen;
import com.jankuester.ggj.twentyseventeen.screens.LoadingScreen;
import com.jankuester.ggj.twentyseventeen.screens.Optionsscreen;
import com.jankuester.ggj.twentyseventeen.screens.PreviewScreen;
import com.jankuester.ggj.twentyseventeen.screens.ScreenBase;
import com.jankuester.ggj.twentyseventeen.screens.StartScreen;
import com.jankuester.ggj.twentyseventeen.screens.actions.ScreenMenuActions;
import com.jankuester.ggj.twentyseventeen.screens.components.ScreenMenuTextButton;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenFactory;
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
	    startScreen();
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
	    startScreen = ScreenFactory.createStartScreen(menuListener);
	queueScreen(startScreen);
    }
    

    
    public void optionsScreen() {
	if (optionsSreen == null)
	    optionsSreen = ScreenFactory.createOptionsScreen(menuListener);
	queueScreen(optionsSreen);
    }

    private void previewMapsScreen() {
	if (previewMapScreen == null)
	previewMapScreen = ScreenFactory.createPreviewMapScreen(menuListener);
	queueScreen(previewMapScreen);
    }

    private void previewVehiclesScreen() {
	if (previewVehicleScreen == null)
	    previewVehicleScreen = ScreenFactory.createPreviewVehicleScreen(menuListener);
	queueScreen(previewVehicleScreen);
    }

    private void gameScreen() {

	GameScreen gameScreen = new GameScreen();
	gameScreen.addInputListener(menuListener);

	// get selection from the other screens
	String selectedMap = previewMapScreen.getCurrentPreview().getId();
	gameScreen.setMapId(selectedMap);
	
	queueScreen(gameScreen);
	unloadMenuScreens();
    }

    private void unloadMenuScreens() {
	unload(startScreen);
	unload(optionsSreen);
	unload(errorScreen);
	unload(loadingScreen);
	unload(previewMapScreen);
	unload(previewVehicleScreen);
    }
    
    private void unload(Screen d){
	if (d != null){
	    d.dispose();
	    d = null;
	}
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
