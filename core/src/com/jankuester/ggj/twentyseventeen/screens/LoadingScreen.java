package com.jankuester.ggj.twentyseventeen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenComponentFactory;

public class LoadingScreen extends ScreenBase {

    public LoadingScreen() {

    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	stage.act();

	spriteBatch.begin();
	if (this.backgroundImage != null) {
	    spriteBatch.draw(backgroundImage, 0, 0, width, height);
	}
	font.draw(spriteBatch, "loading...", 20, height/3);
	spriteBatch.end();

    }
}
