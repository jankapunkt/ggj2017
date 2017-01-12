package com.jankuester.ggj.twentyseventeen.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.maps.Sun;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;

public class PreviewScreen extends ScreenBase {

    private final ArrayList<ModelPreview> previewModels;
    private final ModelBatch modelBatch;

    private Camera perspectiveCam;

    private float rot = 0;

    private int currentPreview = 0;
    private Environment environment;

    public PreviewScreen() {
	previewModels = new ArrayList<ModelPreview>();
	modelBatch = new ModelBatch();
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

	environment = new Environment();
	environment.add(Sun.createSun(0, 30, 0, Color.WHITE, 20000).getLight());

	perspectiveCam = new PerspectiveCamera(75.0f, width, height);
	perspectiveCam.position.set(0, 5, 15);
	// camera.position.rotate(Vector3.Y, -45);
	// camera.lookAt(Vector3.Z);
	camera.lookAt(Vector3.Zero);
	camera.near = 0.1f;
	camera.far = 1000f;
	
	font = new BitmapFont();
    }

    @Override
    public void render(float delta) {

	ModelPreview currentPreview = getCurrentPreview();
	camera.position.set(500, 500, 500);

	// world step
	currentPreview.getModel().transform.rotate(Vector3.Y, delta * 6f);
	stage.act();

	// d clear gl
	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);

	// draw background
	if (this.backgroundImage != null) {
	    spriteBatch.begin();
	    spriteBatch.draw(backgroundImage, 0, 0, width, height);
	    spriteBatch.end();
	}

	// draw current model
	modelBatch.begin(perspectiveCam);
	modelBatch.render(currentPreview.getModel(), environment);
	modelBatch.end();
	
	

	// draw buttons
	if (this.uiElements.size() > 0) {
	    spriteBatch.setProjectionMatrix(camera.combined);
	    spriteBatch.begin();
	    stage.draw();
	    font.draw(spriteBatch, currentPreview.getName(), 20, 20);
	    font.draw(spriteBatch, currentPreview.getDescription(), 20, 80);
	    spriteBatch.end();
	}
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
