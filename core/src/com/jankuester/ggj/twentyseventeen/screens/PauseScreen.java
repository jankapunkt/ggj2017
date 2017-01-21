package com.jankuester.ggj.twentyseventeen.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PauseScreen extends ScreenBase {

    public PauseScreen() {
	// TODO Auto-generated constructor stub
    }

    public void create() {
	super.create();
    }

    @Override
    public void show() {
	super.show();
    }

    @Override
    public void render(float delta) {
	System.out.println("render");
	spriteBatch.begin();
	if (backgroundImage != null)
	    spriteBatch.draw(backgroundImage, 0, 0, width, height);
	spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
	super.resize(width, height);
    }

    @Override
    public void pause() {
	super.pause();
    }

    @Override
    public void resume() {
	super.resume();
    }

    @Override
    public void hide() {
	super.hide();
    }

    @Override
    public void dispose() {
	super.dispose();
    }

    public void setHeight(int screenHeight) {
	this.height = screenHeight;
    }

    public void setWidth(int screenWidth) {
	this.width = screenWidth;
    }

}
