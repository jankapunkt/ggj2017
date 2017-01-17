package com.jankuester.ggj.twentyseventeen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btPersistentManifold;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.jankuester.ggj.twentyseventeen.bullet.CollisionDefs;
import com.jankuester.ggj.twentyseventeen.bullet.CollisionUtils;
import com.jankuester.ggj.twentyseventeen.graphics.rendering.EntityRenderingHelper;
import com.jankuester.ggj.twentyseventeen.graphics.shader.ColorShader;
import com.jankuester.ggj.twentyseventeen.models.characters.KinematicCharacter;
import com.jankuester.ggj.twentyseventeen.models.entities.EntityManager;
import com.jankuester.ggj.twentyseventeen.models.maps.Sun;
import com.jankuester.ggj.twentyseventeen.models.maps.WorldMap;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelFactory;
import com.jankuester.ggj.twentyseventeen.system.GlobalGameSettings;

public class GameScreen extends ScreenBase implements InputProcessor, IItemMenuListener {

    public GameScreen() {
	entityManager = new EntityManager();
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    //
    // GLOBAL VALUES
    //
    ////////////////////////////////////////////////////////////////////////////////////////

    protected MyContactListener contactListener;
    private final EntityManager entityManager;
    private boolean paused = false;

    // TODO move to initMap()
    private String mapId;
    private int mapDifficulty;

    // TODO move to initVehicle()
    private String vehicleId;
    private int vehicleSpeed;
    private int vehicleShield;
    private int vehicleAgility;

    public void setMapId(String mapId) {
	this.mapId = mapId;
    }

    public void setVehicleId(String vehicleId) {
	this.vehicleId = vehicleId;
    }

    public void setMapDifficulty(int mapDifficulty) {
	this.mapDifficulty = mapDifficulty;
    }

    public void setVehicleAgility(int vehicleAgility) {
	this.vehicleAgility = vehicleAgility;
    }

    public void setVehicleShield(int vehicleShield) {
	this.vehicleShield = vehicleShield;
    }

    public void setVehicleSpeed(int vehicleSpeed) {
	this.vehicleSpeed = vehicleSpeed;
    }

    @Override
    public void show() {
	oldx = screenWidth = Gdx.graphics.getWidth();
	oldy = screenHeight = Gdx.graphics.getHeight();

	// --------------------------------------------
	// LOAD UI AND GRAPHICS
	// --------------------------------------------
	loadGraphics();
	loadUI();

	// --------------------------------------------
	// LOAD ENVIRONMENT
	// --------------------------------------------
	loadEnvironment();
	
	// --------------------------------------------
	// LOAD BULLET BEFORE MODELS
	// --------------------------------------------
	loadBulletPhysics();

	// --------------------------------------------
	// LOAD MAP AND OBJECTS
	// --------------------------------------------
	loadMap();
	loadMapObjectsAndItems();

	// --------------------------------------------
	// LOAD PLAYER
	// --------------------------------------------
	loadPlayer();

	// --------------------------------------------
	// INPUT SETTINGS
	// --------------------------------------------
	loadInputSettings();

	// --------------------------------------------
	// LOAD SOUNDS
	// --------------------------------------------
	loadSounds();

    }

    // --------------------------------------------------------------------------------------------------
    // LOAD UI
    // --------------------------------------------------------------------------------------------------
    protected int screenWidth = 0;
    protected int screenHeight = 0;

    private PauseScreen pauseScreen;

    private void loadUI() {
	pauseScreen = new PauseScreen();
	pauseScreen.setWidth(screenWidth);
	pauseScreen.setHeight(screenHeight);
	pauseScreen.create();
    }

    // --------------------------------------------------------------------------------------------------
    // LOAD GRAPHICS SHADERS AND BUFFERS
    // --------------------------------------------------------------------------------------------------
    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;
    private FrameBuffer fboScreenPause;
    private ShaderProgram toonifyShader;

    protected boolean screenBuffered = false;
    protected boolean celShading = false;

    private Texture screenBuffer;

    private ColorShader colorShader;
    private EntityRenderingHelper entityRenderingHelper;

    private void loadGraphics() {
	System.out.println("Load Graphics");
	modelBatch = new ModelBatch();
	spriteBatch = new SpriteBatch();
	entityRenderingHelper = new EntityRenderingHelper();

	// FBO RENDERER
	fboScreenPause = new FrameBuffer(Format.RGB888, this.screenWidth / 32, this.screenHeight / 32, true);

	/*
	 * TODO implement toonifyShader = new
	 * ShaderProgram(Gdx.files.internal("shaders/toon/toonify.vertex.glsl"),
	 * Gdx.files.internal("shaders/toon/toonify.fragment.glsl")); if
	 * (!toonifyShader.isCompiled()) {
	 * System.out.println(toonifyShader.getLog()); }
	 */

	// TODO put shader in options menu
	colorShader = new ColorShader();
	colorShader.init();
    }

    private void loadInputSettings() {
	System.out.println("loadInputSettings");
	cursorCatched = true;

	Gdx.input.setInputProcessor(this);
	Gdx.input.setCursorPosition(screenWidth / 2, screenHeight / 2);
	Gdx.input.setCursorCatched(cursorCatched);
	System.out.println("loadInputSettings done");
    }

    // --------------------------------------------------------------------------------------------------
    // LOAD ENVIRONMENT
    // --------------------------------------------------------------------------------------------------

    private Environment environment;
    private PointLight pointLight;
    private Sun sun;

    private void loadEnvironment() {
	System.out.println("loadEnvironment");
	// sun = Sun.createSun(-4, -2, -4, Color.WHITE, 50);
	environment = new Environment();
	// environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f,
	// 0.6f, 0.6f, 1f));
	// environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
	// -0.8f, -0.2f));
	// environment.add(sun.getLight());
	environment.add(Sun.createSun(0, 200, 0, Color.WHITE, 20000).getLight());
	System.out.println("loadEnvironment done");
    }

    // --------------------------------------------------------------------------------------------------
    // CREATE PLAYER
    // --------------------------------------------------------------------------------------------------

    public KinematicCharacter player;
    private Model playerModel;
    public Vector3 playerPos;
    boolean playerHitsGround;

    private void loadPlayer() {
	playerModel = ModelFactory.getG3DBModel("models/vehicles/medium/vehicle_mid.g3db");
	
	player = new KinematicCharacter(playerModel, "player", 0, 1, 0);
	player.initCamera(screenWidth, screenHeight);
	dynamicsWorld.addCollisionObject(player.ghost, 
		(short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
		(short)(btBroadphaseProxy.CollisionFilterGroups.StaticFilter
		      | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
	dynamicsWorld.addAction(player.charControl);
    }

    // --------------------------------------------------------------------------------------------------
    // CREATE MAP AND OBJECTS
    // --------------------------------------------------------------------------------------------------

    private WorldMap map;

    private void loadMapObjectsAndItems() {
	System.out.println("loadMapObjectsAndItems");
	// create some boxes to collide
	for (int i = 0; i < 15; i++) {
	    for (int j = 0; j < 5; j++) {
		//dynamicsWorld.addRigidBody(map.create("models/maps/simple_terrain/obstacles/box4.g3db", i * 2, j * 2, 0, 50f).getBody());
	    }
	}
	System.out.println("loadMapObjectsAndItems done");
    }

    private void loadMap() {
	System.out.println("loadMap");
	map = new WorldMap();
	
	
	
	btRigidBody ground = 
		//map.createTerrain("models/maps/easy/models/city_ground.g3db").getBody();
		//map.createTerrain("models/maps/simple_terrain/simple_terrain.g3db").getBody();
		map.createTerrain("ground", 50f,1f, 50f, new Vector3(0,0,0), Color.BLUE).getBody();
		//map.createTerrain("models/maps/easy/preview/preview_city.g3db").getBody();

	dynamicsWorld.addRigidBody(ground);
	ground.setFriction(650); // TODO make friction compatible with our
				 // vehicle speed/agility and so on
	ground.setContactCallbackFlag(CollisionDefs.GROUND_FLAG);
	ground.setContactCallbackFilter(0);
	System.out.println("loadMap done");
    }

    // --------------------------------------------------------------------------------------------------
    // CREATE BULLET AND DYNAMICS WORLD
    // --------------------------------------------------------------------------------------------------

    protected btCollisionConfiguration collisionConfig;
    protected btDispatcher dispatcher;
    protected btDbvtBroadphase broadphase;
    protected btDynamicsWorld dynamicsWorld;
    protected btConstraintSolver constraintSolver;
    protected DebugDrawer debugDrawer;

    public final float GRAVITY = 17f;

    private float deltaTime;
    private float worldSpeed = 1.0f;

    private boolean bulletDebug;
    private boolean cursorCatched;

    private void loadBulletPhysics() {
	System.out.println("loadBulletPhysics");
	Bullet.init();
	collisionConfig = new btDefaultCollisionConfiguration();
	dispatcher = new btCollisionDispatcher(collisionConfig);
	broadphase = new btDbvtBroadphase();
	constraintSolver = new btSequentialImpulseConstraintSolver();
	contactListener = new MyContactListener();
	dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
	dynamicsWorld.setGravity(new Vector3(0, -9.81f, 0));

	// debug
	debugDrawer = new DebugDrawer();
	debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
	dynamicsWorld.setDebugDrawer(debugDrawer);
	System.out.println("loadBulletPhysics done");
    }

    // --------------------------------------------------------------------------------------------------
    // CREATE PARTICLE SYSTEM
    // --------------------------------------------------------------------------------------------------

    protected ParticleSystem particleSystem;
    protected PointSpriteParticleBatch pointSpriteBatch;
    protected AssetManager assets;
    protected ParticleEffect currentEffects;
    protected Matrix4 particleMatrix;

    protected void createParticleSystem() {
	System.out.println("createParticleSystem");
	particleSystem = ParticleSystem.get();
	pointSpriteBatch.setCamera(player.camera);
	particleSystem.add(pointSpriteBatch);

	assets = new AssetManager();
	ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(
		particleSystem.getBatches());
	ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
	assets.setLoader(ParticleEffect.class, loader);
	assets.load("particles/shotA.pfx", ParticleEffect.class, loadParam);
	assets.finishLoading();

	particleMatrix = new Matrix4();
	System.out.println("createParticleSystem done");
    }

    // --------------------------------------------------------------------------------------------------
    // CREATE SOUND SYSTEM
    // --------------------------------------------------------------------------------------------------

    protected Music musicBg;
    private FrameBuffer src;
    private TextureRegion celSpriteRegion;

    protected void loadSounds() {
	System.out.println("loadSounds");
	musicBg = Gdx.audio.newMusic(Gdx.files.internal("audio/maps/city_map_bg.mp3"));
	musicBg.setVolume(GlobalGameSettings.loudeness_music);
	musicBg.setLooping(true);
	musicBg.play();
	System.out.println("loadSounds done");
    }

    // ================================================================================================
    //
    // RENDERING
    //
    // ================================================================================================

    @Override
    public void render(float delta) {

	if (!this.paused) {
	    doWorldStep();
	    if (this.celShading) {
		renderOnScreen(colorShader);
	    } else {
		renderOnScreen(null);
	    }
	} else {
	    renderOffScreen();
	    pauseScreen.render(delta);
	}

    }

    private void renderOnScreen(Shader shader) {
	beforeRenderScene();
	renderScene(shader);
    }

    private void beforeRenderScene() {
	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	Gdx.gl.glClearColor(0.7f, 0.7f, 0.9f, 4f);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
    }

    private void doWorldStep() {
	deltaTime = Math.min(1f / 30f, Gdx.graphics.getDeltaTime()) * worldSpeed; // max
	dynamicsWorld.stepSimulation(deltaTime, 5, 1f / 60f); // step world

	// POST WORLDSTEP
	player.update(deltaTime);
	
    }

    private void renderOffScreen() {
	if (!screenBuffered) {
	    fboScreenPause.begin();
	    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
	    Gdx.gl.glClearColor(0.7f, 0.7f, 0.9f, 4f);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	    renderScene(null);
	    fboScreenPause.end();
	    screenBuffer = fboScreenPause.getColorBufferTexture();
	    pauseScreen.setBackgroundImage(screenBuffer, true); // this will be
								// used // in
	    screenBuffered = true;
	}
    }

    private void renderScene(Shader shader) {
	entityRenderingHelper.begin(player.camera, modelBatch);
	entityRenderingHelper.render(map.getRenderingInstances(), environment, shader);
	entityRenderingHelper.render(player, environment, shader);
	entityRenderingHelper.end();

	if (bulletDebug) {
	    debugDrawer.begin(player.camera);
	    dynamicsWorld.debugDrawWorld();
	    debugDrawer.end();
	}
    }

    @Override
    public void resize(int width, int height) {
	// TODO Auto-generated method stub

    }

    @Override
    public void pause() {
	System.out.println("pause");
	this.paused = true;
    }

    @Override
    public void resume() {
	System.out.println("resume");
	this.paused = false;
	this.screenBuffered = false;
    }

    @Override
    public void hide() {
	// TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
	fboScreenPause.dispose();
	dispatcher.dispose();
	collisionConfig.dispose();
	player.dispose();
	modelBatch.dispose();
	pauseScreen.dispose();
	screenBuffer.dispose();
	fboScreenPause.dispose();
	map.dispose();
	if (playerModel != null)
	    playerModel.dispose();
	if (contactListener != null)
	    contactListener.dispose();
	if (currentEffects != null)
	    currentEffects.dispose();
	if (assets != null)
	    assets.dispose();
	if (musicBg != null)
	    musicBg.dispose();

    }

    // ====================================================================
    //
    // CONTACT / COLLISION SYSTEM
    //
    // ====================================================================

    class MyContactListener extends ContactListener {

	@Override
	public void onContactEnded(int userValue0, boolean match0, int userValue1, boolean match1) {
	    if (match0) {
		player.hitsGround = false;
	    }
	}

	@Override
	public void onContactStarted(btPersistentManifold manifold, boolean match0, boolean match1) {
	    int id0 = manifold.getBody0().getUserValue();
	    int id1 = manifold.getBody1().getUserValue();
	    int playerval = player.ghost.getUserValue();
	    int callbackid;

	    if (match0 && id0 == playerval) {
		callbackid = manifold.getBody1().getContactCallbackFlag();
	    } else if (match1 && id1 == playerval) {
		callbackid = manifold.getBody0().getContactCallbackFlag();
	    } else {
		return;
	    }

	    if (callbackid == CollisionDefs.WEAPON_FLAG) {
		// TODO add weapons if desired
	    }
	}
    }

    // ====================================================================
    //
    // INPUT EVENT SYSTEM
    //
    // ====================================================================

    @Override
    public boolean keyDown(int keycode) {

	// OPEN PAUSE SCREEN
	if (keycode == Input.Keys.ESCAPE) {
	    cursorCatched = !cursorCatched;
	    Gdx.input.setCursorCatched(cursorCatched);
	    if (this.paused) {
		Gdx.input.setCursorPosition(oldx, oldy);
		this.resume();
	    } else {
		Gdx.input.setCursorPosition(screenWidth / 2, screenHeight / 2);
		this.pause();
	    }
	}

	if (!cursorCatched) {
	    resetPlayerMovements();
	    return false;
	}

	if (keycode == Input.Keys.SHIFT_LEFT) {
	    player.setShiftDown(true);
	    return true;
	}

	if (keycode == Input.Keys.LEFT || keycode == 29) {
	    player.setLeftMove(true);
	    return true;
	}
	if (keycode == Input.Keys.RIGHT || keycode == 32) {
	    player.setRightMove(true);
	    return true;
	}
	if (keycode == Input.Keys.UP || keycode == 51) {
	    player.setMoveForward(true);
	    return true;
	}
	if (keycode == Input.Keys.DOWN || keycode == 47) {
	    player.setMoveBack(true);
	    return true;
	}
	if (keycode == Input.Keys.T) {
	    this.celShading = !this.celShading;
	    return true;
	}
	if (keycode == Input.Keys.SPACE) {
	    player.setJumpMove(true);
	    return true;
	}
	if (keycode == Input.Keys.B) {
	    bulletDebug = !bulletDebug;
	    return true;
	}
	if (keycode == Input.Keys.C) {
	    player.switchCameraMode();
	    return true;
	}

	if (keycode == Input.Keys.E) {
	    this.worldSpeed = 0f;
	    return true;
	}

	return false;
    }

    private void resetPlayerMovements() {
	player.setShiftDown(false);
	player.setJumpMove(false);
	player.setLeftMove(false);
	player.setRightMove(false);
	player.setMoveForward(false);
	player.setMoveBack(false);
    }

    @Override
    public boolean keyUp(int keycode) {
	if (!cursorCatched)
	    return false;

	if (keycode == Input.Keys.SHIFT_LEFT) {
	    player.setShiftDown(false);
	    return true;
	}

	if (keycode == Input.Keys.LEFT || keycode == 29) {
	    player.setLeftMove(false);
	    return true;
	}
	if (keycode == Input.Keys.RIGHT || keycode == 32) {
	    player.setRightMove(false);
	    return true;
	}
	if (keycode == Input.Keys.UP || keycode == 51) {
	    player.setMoveForward(false);
	    return true;
	}
	if (keycode == Input.Keys.DOWN || keycode == 47) {
	    player.setMoveBack(false);
	    return true;
	}

	if (keycode == Input.Keys.E) {
	    this.worldSpeed = 1f;
	    return true;
	}

	return false;
    }

    @Override
    public boolean keyTyped(char character) {
	return false;
    }

    // --------------------------------------------------------------------------------------------------
    // SHOOTING / ACTIONS
    // --------------------------------------------------------------------------------------------------

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	if (!cursorCatched)
	    return false;
	return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
	return true;
    }

    // --------------------------------------------------------------------------------------------------
    // MOUSE MOVE / CAMERA MOVE
    // --------------------------------------------------------------------------------------------------

    protected boolean isMouseMoveLeft = false;
    protected boolean isMouseMoveRight = false;

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
	mouseMove(screenX, screenY);
	return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
	mouseMove(screenX, screenY);
	return true;
    }

    protected int oldx = -1;
    protected int oldy = -1;

    protected void mouseMove(int screenX, int screenY) {
	if (!cursorCatched)
	    return;

	float delta = Gdx.graphics.getDeltaTime();
	int dx = screenX - oldx;
	int dy = screenY - oldy;

	player.rotateCamera(dx, dy, delta);

	oldx = screenX;
	oldy = screenY;

	if (dx < 0) {
	    isMouseMoveRight = false;
	    isMouseMoveLeft = true;
	}
	if (dx > 0) {
	    isMouseMoveRight = true;
	    isMouseMoveLeft = false;
	}
    }

    public boolean scrolled(int amount) {
	player.setCamDistance(amount);
	return true;
    }

}
