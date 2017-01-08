package com.jankuester.ggj.twentyseventeen.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jankuester.ggj.twentyseventeen.GGJTwentySeventeenGame;
import com.jankuester.ggj.twentyseventeen.system.GlobalGameSettings;
import com.sun.prism.image.ViewPort;

public class ScreenBase implements Screen {

    protected GGJTwentySeventeenGame game;
    
    protected int width = GlobalGameSettings.resolutionX;
    protected int height = GlobalGameSettings.resolutionY;
    
    protected Table menuTable;
    protected Stage stage;
    protected OrthographicCamera camera;
    protected SpriteBatch batch;

    protected ArrayList<TextButton> buttons;
    protected ArrayList<InputListener> listeners;

    public ScreenBase() {
	buttons = new ArrayList<TextButton>();
	listeners = new ArrayList<InputListener>();
    }

    public void create() {
	camera = new OrthographicCamera();
	camera.setToOrtho(false, width, height); // ** w/h ratio = 1.66 **//
	batch = new SpriteBatch();
	resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	stage = new Stage();
	stage.clear();
	Gdx.input.setInputProcessor(stage); // ** stage is responsive **//

	menuTable = new Table();

	for (TextButton textButton : buttons) {
	    menuTable.row(); // new row entry
	    for (InputListener listener : listeners) {
		textButton.addListener(listener);
	    }
	    menuTable.add(textButton);
	}

	menuTable.align(Align.center);
	menuTable.setPosition(this.width/2, this.height / 2);
	stage.addActor(menuTable);
    }
    
    @Override
    public void show() {
	create();
    }

    public void addInputListener(InputListener listener) {
	listeners.add(listener);
    }

    public void addButton(TextButton tb) {
	buttons.add(tb);
    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	stage.act();

	batch.setProjectionMatrix(camera.combined);
	batch.begin();
	stage.draw();
	batch.end();

    }

    @Override
    public void resize(int width, int height) {
	
	Vector2 size = Scaling.fit.apply(800, 480, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
	this.width = width;
	this.height = height;
	Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
	
	if (stage!=null)
	    stage.getViewport().update(width, height, false);
	if (camera != null)
	    camera.update();
    }

    @Override
    public void pause() {
	// TODO Auto-generated method stub

    }

    @Override
    public void resume() {
	// TODO Auto-generated method stub

    }

    @Override
    public void hide() {
	// TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
	// TODO Auto-generated method stub

    }

}
