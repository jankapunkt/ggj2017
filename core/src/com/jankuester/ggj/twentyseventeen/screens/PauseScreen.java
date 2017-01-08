package com.jankuester.ggj.twentyseventeen.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PauseScreen extends ScreenBase {

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
	// Gdx.gl.glClearColor(1, 1, 1, 1);
	// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	// stage.act();
	// batch.setProjectionMatrix(camera.combined);
	spriteBatch.begin();
	spriteBatch.draw(background, 0, 0, width, height);
	// stage.draw();
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
	if (background != null && background.getTexture() != null)
	    background.getTexture().dispose();
	super.dispose();
    }

    public void setHeight(int screenHeight) {
	this.height = screenHeight;
    }

    public void setWidth(int screenWidth) {
	this.width = screenWidth;
    }

}
