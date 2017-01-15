package com.jankuester.ggj.twentyseventeen.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.maps.Sun;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenComponentFactory;

public class PreviewScreen extends ScreenBase {

    private final ArrayList<ModelPreview> previewModels;
    private final ModelBatch modelBatch;
    private final Environment environment;

    private Camera perspectiveCam;
    private Sun sun_left, sun_right;

    private float rot = 0;

    private int currentPreview = 0;

    public PreviewScreen() {
	previewModels = new ArrayList<ModelPreview>();
	modelBatch = new ModelBatch();
	environment = new Environment();
	environment.clear();
    }

    public boolean addPreviewModel(ModelPreview previewModel) {
	return previewModels.add(previewModel);
    }

    public GameModelInstance getCurrentModel() {
	return getCurrentPreview().getModel();
    }

    public ModelPreview getCurrentPreview() {
	return previewModels.get(currentPreview);
    }

    public void next() {
	if (currentPreview < previewModels.size() - 1)
	    currentPreview++;
    }

    public void previous() {
	if (currentPreview > 0)
	    currentPreview--;
    }

    @Override
    public void create() {
	super.create();

	// environment.add(Sun.createSun(0, 30, 0, Color.WHITE,
	// 20000).getLight());

	perspectiveCam = new PerspectiveCamera(75.0f, width, height);
	perspectiveCam.position.set(0, 5, 15);
	// camera.position.rotate(Vector3.Y, -45);
	// camera.lookAt(Vector3.Z);
	camera.lookAt(Vector3.Zero);
	camera.near = 0.1f;
	camera.far = 200f;

	font = ScreenComponentFactory.mediumFont;

	// sun_left = Sun.createSun(0, 5, 15, Color.GOLD, 1500);
	// environment.add(Sun.createPointLight(0, 0, 0, Color.WHITE, 5000));
	// environment.add(new PointLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f,
	// -0.8f, 100));
    }

    @Override
    public void render(float delta) {

	ModelPreview currentPreview = getCurrentPreview();
	camera.position.set(500, 500, 500);

	// world step
	currentPreview.getModel().transform.rotate(Vector3.Y, delta * 6f);
	stage.act();

	// clear gl
	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);

	SpriteBatch stagebatch = (SpriteBatch) stage.getBatch();

	// draw background
	if (this.backgroundImage != null) {
	    stagebatch.begin();
	    stagebatch.draw(backgroundImage, 0, 0, width, height);
	    stagebatch.end();
	}

	// draw current model
	modelBatch.begin(perspectiveCam);
	modelBatch.render(currentPreview.getModel(), environment);
	modelBatch.end();

	// draw buttons
	if (this.uiElements.size() > 0) {
	    // spriteBatch.setProjectionMatrix(camera.combined);
	    spriteBatch.begin();
	    stage.draw();
	    spriteBatch.end();
	}

	spriteBatch.begin();
	font.draw(spriteBatch, currentPreview.getName(), 20, height - 20);
	font.draw(spriteBatch, currentPreview.getDescription(), 20, height - 60);
	font.draw(spriteBatch, "Difficulty: " + currentPreview.getDifficultyString() , 20, height - 100);
	spriteBatch.end();

    }

    @Override
    public void dispose() {
	modelBatch.dispose();
	previewModels.clear();
	font.dispose();
	super.dispose();
    }

    @Override
    public void resize(int width, int height) {
	super.resize(width, height);
	if (perspectiveCam != null)
	    perspectiveCam.update();
    }
}
