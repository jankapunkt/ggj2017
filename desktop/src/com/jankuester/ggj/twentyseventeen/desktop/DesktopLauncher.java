package com.jankuester.ggj.twentyseventeen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jankuester.ggj.twentyseventeen.GGJTwentySeventeenGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GGJTwentySeventeenGame(), config);
	}
}
