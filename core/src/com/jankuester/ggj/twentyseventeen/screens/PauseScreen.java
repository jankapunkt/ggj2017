package com.jankuester.ggj.twentyseventeen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PauseScreen extends BaseScreen {

    private TextureRegion background;

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
	//Gdx.gl.glClearColor(1, 1, 1, 1);
	//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	//stage.act();
	//batch.setProjectionMatrix(camera.combined);
	batch.begin();
	batch.draw(background, 0, 0, _width, _height);
	//stage.draw();
	batch.end();
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
	if (background != null && background.getTexture() != null)
	    background.getTexture().dispose();
	super.dispose();
    }

    public void setBackgroundImage(Texture screenBuffer) {
	screenBuffer.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	TextureRegion region = new TextureRegion(screenBuffer);
	region.flip(false, true);
	this.background = region;
    }

    public void setHeight(int screenHeight) {
	this._height = screenHeight;
    }

    public void setWidth(int screenWidth) {
	this._width = screenWidth;
    }

}
