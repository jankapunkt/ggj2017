package com.jankuester.ggj.twentyseventeen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jankuester.ggj.twentyseventeen.GGJTwentySeventeenGame;
import com.jankuester.ggj.twentyseventeen.system.GlobalGameSettings;

public class DesktopLauncher {
    public static void main(String[] arg) {
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.fullscreen = true;
	config.resizable = true;
	config.width = GlobalGameSettings.resolutionX;
	config.height = GlobalGameSettings.resolutionY;
	new LwjglApplication(new GGJTwentySeventeenGame(), config);
    }
}
