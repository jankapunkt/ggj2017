package com.jankuester.ggj.twentyseventeen.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.jankuester.ggj.twentyseventeen.GGJTwentySeventeenGame;

public class BaseScreen implements Screen {

    protected GGJTwentySeventeenGame game;
    protected SpriteBatch batch;
    protected int _width, _height;
    protected Table menuTable;
    protected Stage stage;
    protected OrthographicCamera camera;

    protected ArrayList<TextButton> buttons;
    protected ArrayList<InputListener> listeners;

    public BaseScreen() {
	buttons = new ArrayList<TextButton>();
	listeners = new ArrayList<InputListener>();
    }

    public void create() {
	camera = new OrthographicCamera();
	camera.setToOrtho(false, 800, 480); // ** w/h ratio = 1.66 **//
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
	menuTable.setPosition(_width/2, _height / 2);
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
	_width = width;
	_height = height;
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
