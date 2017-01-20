package com.jankuester.ggj.twentyseventeen.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jankuester.ggj.twentyseventeen.logic.ModelMap;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.environment.Sun;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;
import com.jankuester.ggj.twentyseventeen.screens.actions.ScreenMenuActions;
import com.jankuester.ggj.twentyseventeen.screens.components.ScreenMenuTextButton;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenComponentFactory;

public class PreviewScreen extends ScreenBase {

    private final ArrayList<ModelPreview> previewModels;
    private final ModelBatch modelBatch;
    private final Environment environment;

    private Camera perspectiveCam;
    private Sun sun_left, sun_right;

    private int currentPreview = 0;
    private ScreenMenuTextButton prevButton;
    private ScreenMenuTextButton nextButton;
    
    

    public PreviewScreen() {
	previewModels = new ArrayList<ModelPreview>();
	modelBatch = new ModelBatch();
	environment = new Environment();
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
    
    public boolean hasNext(){
	return currentPreview < previewModels.size() - 1;
    }

    public boolean hasPrevious(){
	return currentPreview > 0;
    }
    
    public void next() {
	if (hasNext())
	    currentPreview++;
    }

    public void previous() {
	if (hasPrevious())
	    currentPreview--;
    }

    @Override
    public void create() {
	super.create();

	
	nextButton = ScreenComponentFactory.createMenuButton(ScreenMenuActions.PREVIOUS, "<", 300, 200, 10, height/2);
	stage.addActor(nextButton);
	
	prevButton = ScreenComponentFactory.createMenuButton(ScreenMenuActions.NEXT, ">", 300, 200, width-300, height/2);
	stage.addActor(prevButton);
	
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
	environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	updateMenuButtons();
    }

    private void updateMenuButtons() {
	nextButton.setDisabled(!hasNext());
	prevButton.setDisabled(!hasPrevious());
    }

    @Override
    public void render(float delta) {
	ModelPreview currentPreview = getCurrentPreview();

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
	if (currentPreview.getType() == ModelMap.MAP){
	    font.draw(spriteBatch, "Difficulty: " + currentPreview.getDifficultyString(), 20, height - 100);
	}
	if (currentPreview.getType() == ModelMap.VEHICLE){
	    font.draw(spriteBatch, "Speed: " + currentPreview.getSpeedString(), 20, height - 100);
	    font.draw(spriteBatch, "Agile: " + currentPreview.getAgilityString(), 20, height - 140);
	    font.draw(spriteBatch, "Shield: " + currentPreview.getShieldString(), 20, height - 180);
	}
	spriteBatch.end();

    }

    @Override
    public void dispose() {
	modelBatch.dispose();
	previewModels.clear();
	super.dispose();
    }

    @Override
    public void resize(int width, int height) {
	super.resize(width, height);
	if (perspectiveCam != null)
	    perspectiveCam.update();
    }

    public void onMenuAction(int action) {
	switch (action) {
	case ScreenMenuActions.NEXT:
	    next();
	    updateMenuButtons();
	    break;
	case ScreenMenuActions.PREVIOUS:
	    previous();
	    updateMenuButtons();
	    break;
	default:
	    break;
	}
    }
    
    
    class PreviewListener extends InputListener {
	private PreviewScreen screen;

	public PreviewListener(PreviewScreen screen) {
	    super();
	    this.screen = screen;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
	    if (event.getListenerActor() instanceof ScreenMenuTextButton) {
		ScreenMenuTextButton target = (ScreenMenuTextButton) event.getListenerActor();
		screen.onMenuAction(target.getAction());
	    } else {
		//lastMenuHover = -1;
	    }
	    return super.touchDown(event, x, y, pointer, button);
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
	    return super.mouseMoved(event, x, y);
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
	    return super.keyDown(event, keycode);
	}
    }
}
