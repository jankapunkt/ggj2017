package com.jankuester.ggj.twentyseventeen.screens.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jankuester.ggj.twentyseventeen.logic.ModelMap;
import com.jankuester.ggj.twentyseventeen.models.factories.ModelPreviewFactory;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;
import com.jankuester.ggj.twentyseventeen.screens.ErrorScreen;
import com.jankuester.ggj.twentyseventeen.screens.LoadingScreen;
import com.jankuester.ggj.twentyseventeen.screens.Optionsscreen;
import com.jankuester.ggj.twentyseventeen.screens.PreviewScreen;
import com.jankuester.ggj.twentyseventeen.screens.StartScreen;
import com.jankuester.ggj.twentyseventeen.screens.actions.ScreenMenuActions;

public class ScreenFactory {

    public static LoadingScreen createLoadingScreen() {
	return new LoadingScreen();
    }

    public static ErrorScreen createErrorScreen(Exception e) {
	ErrorScreen err = new ErrorScreen();
	err.setErrorMessage(e.getMessage());
	err.setStackTrace(e.getStackTrace());
	return err;
    }

    public static StartScreen createStartScreen(InputListener menuListener) {
	StartScreen startScreen = new StartScreen();
	Texture startBg = new Texture(Gdx.files.internal("images/title_logo.png"));
	startScreen.setBackgroundImage(startBg, false);
	startScreen.addInputListener(menuListener);

	startScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.GAME, "ARCADE MODE", 300, 200, 0, 0));
	startScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.GAME, "SIMULATION MODE", 300, 200, 0, 0));
	startScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.OPTIONS, "OPTIONS", 300, 200, 0, 0));
	startScreen.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.EXIT, "EXIT", 300, 200, 0, 0));
	startScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	
	startScreen.alignMenu(2);
	
	return startScreen;
    }

    public static Optionsscreen createOptionsScreen(InputListener menuListener) {
	Optionsscreen optionsSreen = new Optionsscreen();
	optionsSreen.addInputListener(menuListener);
	optionsSreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", 300, 100, 0, 0));
	optionsSreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	return optionsSreen;
    }

    public static PreviewScreen createPreviewMapScreen(InputListener menuListener) {
	PreviewScreen previewMapScreen = new PreviewScreen();
	Texture background = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	previewMapScreen.setBackgroundImage(background, false);
	previewMapScreen.addInputListener(menuListener);
	previewMapScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIEW_VEHICLES, "SELECT MAP", 300, 200, 0, 0));
	previewMapScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.START, "BACK TO MAIN MENU", 300, 200, 0, 0));

	ModelPreview cityMapPreview = ModelPreviewFactory.createMapPreview(ModelMap.Map.MAP_EASY);

	previewMapScreen.addPreviewModel(cityMapPreview);
	return previewMapScreen;
    }

    public static PreviewScreen createPreviewVehicleScreen(InputListener menuListener) {
	PreviewScreen previewVehicleScreen = new PreviewScreen();
	Texture background = new Texture(Gdx.files.internal("images/bg_space.jpg"));
	previewVehicleScreen.setBackgroundImage(background, false);
	previewVehicleScreen.addInputListener(menuListener);
	previewVehicleScreen.addButton(
		ScreenComponentFactory.createMenuButton(ScreenMenuActions.GAME, "START RACE", 300, 100, 0, 0));
	previewVehicleScreen.addButton(ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIEW_MAPS,
		"BACK TO MAP PREVIEW", 300, 100, 0, 0));

	ModelPreview vehiclePreview_mid = ModelPreviewFactory
		.createVehiclePreview(ModelMap.Vehicle.VEHICLE_MIDDLEWEIGHT);
	vehiclePreview_mid.setScale(4);
	previewVehicleScreen.addPreviewModel(vehiclePreview_mid);
	return previewVehicleScreen;
    }

}
