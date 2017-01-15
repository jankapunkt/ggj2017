package com.jankuester.ggj.twentyseventeen.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.jankuester.ggj.twentyseventeen.GGJTwentySeventeenGame;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenComponentFactory;
import com.jankuester.ggj.twentyseventeen.system.GlobalGameSettings;

public class ScreenBase implements Screen {

    protected GGJTwentySeventeenGame game;

    protected int width = GlobalGameSettings.resolutionX;
    protected int height = GlobalGameSettings.resolutionY;

    protected TextureRegion backgroundImage;

    protected Table menuTable;
    protected Stage stage;
    protected OrthographicCamera camera;
    protected SpriteBatch spriteBatch;
    protected BitmapFont font;

    protected ArrayList<Actor> uiElements;
    protected ArrayList<InputListener> listeners;

    private boolean paused = false;
    private boolean created = false;

    public ScreenBase() {
	uiElements = new ArrayList<Actor>();
	listeners = new ArrayList<InputListener>();
    }

    public void create() {
	font = ScreenComponentFactory.largeFont;
	camera = new OrthographicCamera();
	camera.setToOrtho(false, width, height); // ** w/h ratio = 1.66 **//
	spriteBatch = new SpriteBatch();
	resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	stage = new Stage(new ExtendViewport(1280, 768));
	stage.clear();
	Gdx.input.setInputProcessor(stage); // ** stage is responsive **//

	menuTable = new Table();
	for (Actor uiElement : uiElements) {
	    menuTable.row(); // new row entry
	    for (InputListener listener : listeners) {
		uiElement.addListener(listener);
	    }
	    menuTable.add(uiElement);
	}

	menuTable.align(Align.center);
	menuTable.setPosition(this.width / 2, this.height / 2);
	menuTable.setBackground(ScreenComponentFactory.createBackground(Color.CYAN, 1.0f, 100, 100));
	stage.addActor(menuTable);


    }

    @Override
    public void show() {
	if (!created)
	    create();
    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	stage.act();

	if (this.backgroundImage != null) {
	    spriteBatch.begin();
	    spriteBatch.draw(backgroundImage, 0, 0, width, height);
	    spriteBatch.end();
	}

	if (this.uiElements.size() > 0) {
	    spriteBatch.setProjectionMatrix(camera.combined);
	    spriteBatch.begin();
	    stage.draw();
	    spriteBatch.end();
	}
    }

    @Override
    public void resize(int width, int height) {
	// http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
	Vector2 size = Scaling.fit.apply(800, 480, width, height);
	int viewportX = (int) (width - size.x) / 2;
	int viewportY = (int) (height - size.y) / 2;
	int viewportWidth = (int) size.x;
	int viewportHeight = (int) size.y;
	this.width = width;
	this.height = height;
	Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);

	if (stage != null)
	    stage.getViewport().update(width, height, false);
	if (camera != null)
	    camera.update();
    }

    @Override
    public void pause() {
	this.paused = true;
    }

    @Override
    public void resume() {
	this.paused = false;
    }

    @Override
    public void hide() {
	// TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
	if (listeners != null)
	    listeners.clear();
	if (menuTable != null)
	    menuTable.clear();
	if (uiElements != null)
	    uiElements.clear();
	dispose(spriteBatch);
	dispose(stage);
	if (backgroundImage != null && backgroundImage.getTexture() != null)
	    dispose(backgroundImage.getTexture());
	dispose(font);
    }

    private void dispose(Disposable disposeable) {
	if (disposeable != null) {
	    try {
		disposeable.dispose();
	    } catch (Exception e) {
	    }
	}
    }

    public Table getMenuTable() {
	return menuTable;
    }

    public void addInputListener(InputListener listener) {
	listeners.add(listener);
    }

    public void addText(Label text) {
	this.addUiElement(text);
    }

    public void addButton(TextButton tb) {
	this.addUiElement(tb);
    }

    public void addUiElement(Actor element) {
	uiElements.add(element);
    }

    public void setBackgroundImage(Texture background, boolean fromBuffer) {
	if (backgroundImage != null)
	    backgroundImage.getTexture().dispose();
	if (fromBuffer) {
	    background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	    TextureRegion region = new TextureRegion(background);
	    region.flip(false, true);
	    this.backgroundImage = region;
	} else {
	    this.backgroundImage = new TextureRegion(background);
	}
    }



}
