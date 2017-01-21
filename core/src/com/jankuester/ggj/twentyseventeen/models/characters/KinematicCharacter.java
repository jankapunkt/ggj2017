package com.jankuester.ggj.twentyseventeen.models.characters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.utils.Disposable;
import com.jankuester.ggj.twentyseventeen.bullet.CollisionDefs;
import com.jankuester.ggj.twentyseventeen.bullet.ICollisionTarget;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.items.states.WeaponState;
import com.jankuester.ggj.twentyseventeen.models.items.states.WeaponStates;

/**
 * A character class, extending ModelInstance and using btPairCachingGhostObject
 * and btKinematicCharacterController for controls.
 * 
 * @author Jan Kuester www.jankuester.com
 */
public class KinematicCharacter extends GameModelInstance implements Disposable, ICollisionTarget {
    // =====================================================================================================
    // //
    //
    //
    // MEMBER VARIABLES AND FINALS
    //
    //
    // =====================================================================================================
    // //

    public final String id;

    public final static int CAMERA_MODE_NONE = -1;
    public final static int CAMERA_MODE_FIRST = 0;
    public final static int CAMERA_MODE_THIRD = 1;

    public int currentCameraMode;

    // -------------- collision ---------------------//
    public final btPairCachingGhostObject ghost;
    public final btKinematicCharacterController charControl;

    protected final btConvexShape playerColShape;
    protected final Vector3 localInertia = new Vector3();
    protected final float mass;

    // =====================================================================================================//
    //
    //
    // CONSTRUCTORS
    //
    //
    // =====================================================================================================//

    public KinematicCharacter(Model model, String id, float x, float y, float z) {
	super(model);

	this.id = id;
	mass = 875f;

	currentCameraMode = CAMERA_MODE_THIRD;

	// initial positio = spawn place
	position.set(x, y, z);
	transform.setTranslation(this.position);
	transform.rotate(Vector3.X, 90);
	transform.rotate(Vector3.Y, 180);

	// init capsule shape
	playerColShape = new btSphereShape(0.5f);
	// playerColShape = new btCapsuleShape(0.5f, 2f); //for less computation
	playerColShape.calculateLocalInertia(mass, localInertia);
	
	// ------------------------------------
	// init ghost object
	// this is the invisible
	// collision object
	ghost = new btPairCachingGhostObject();
	ghost.setWorldTransform(this.transform);
	
	ghost.setUserValue(10101010);
	ghost.setCollisionShape(playerColShape);

	ghost.setCollisionFlags(ghost.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
	ghost.setContactCallbackFilter(
		CollisionDefs.OBJECT_FLAG | CollisionDefs.WEAPON_FLAG | CollisionDefs.GROUND_FLAG
		| CollisionDefs.WALL_LEFT | CollisionDefs.WALL_RIGHT | CollisionDefs.ALL_FLAG);
	ghost.setContactCallbackFlag(CollisionDefs.PLAYER_FLAG);

	ghost.setFriction(0);
	
	
	// ------------------------------------
	// init char control
	// this is the controller
	// for transform and stuff
	charControl = new btKinematicCharacterController(ghost, playerColShape, .00001f);
	charControl.setUseGhostSweepTest(false);
	
	charControl.setUp(Vector3.Y);//setUpAxis(1); // y-UP
	charControl.setMaxJumpHeight(5.5f);
	charControl.setJumpSpeed(20);

	// sound shot
	sounds = new HashMap<String, Sound>();
	// sounds.put("walk",
	// Gdx.audio.newSound(Gdx.files.internal("audio/walk.mp3")));
	// sounds.put("jump",
	// Gdx.audio.newSound(Gdx.files.internal("audio/jump.mp3")));

	// TODO this must be put into Weapon class
	// sounds.put("shot1",
	// Gdx.audio.newSound(Gdx.files.internal("audio/item_gun_shot.mp3")));
	// sounds.put("empty",
	// Gdx.audio.newSound(Gdx.files.internal("audio/item_gun_empty.mp3")));

	this.userData = new Color(position.x, position.y, position.z, 1f);
    }

    // =====================================================================================================//
    //
    //
    // SHIELD AND SPEED
    //
    //
    // =====================================================================================================//

    private float maxVelocity= 1;
    private float agility =1;
    
    public void setVehicleAgility(int value){
	agility=value/100;
    }
    
    public void setVehicleSpeed(int value){
	maxVelocity = value/100;
    }
    
   
    private int maxShield=1;
    
    public void setShield(int value){
	currentShield = maxShield = value; 
    }
    
    private int currentShield=0;
    
    public float getShield(){
	return currentShield/maxShield*100;
    }
    
    public float getSpeed(){
	return this.velocity.z * 50;
    }
    
    // =====================================================================================================//
    //
    //
    // MODEL INSTANCE RELATED METHODS
    //
    //
    // =====================================================================================================//

    
    public boolean isCollisionTarget(btCollisionObject toCompare) {
	return toCompare != null && toCompare.equals(ghost);
    }

    /**
     * Updates motion and state values before rendering
     * 
     * @param delta
     *            delta time
     */
    public void update(float delta) {
	updateMotion(delta);
	updateCamera();
    }

    /**
     * Renders this model with a given batch and environment. Additional
     * processing can be done here
     * 
     * @param modelBatch
     *            the model batch to render
     * @param env
     *            the environment to render
     */
    public void render(ModelBatch modelBatch, Environment env, Shader shader) {
	if (shader != null)
	    modelBatch.render(this, env, shader);
	else
	    modelBatch.render(this, env);
    }

    /**
     * {@inheritDoc}
     */
    public void dispose() {
	charControl.dispose();
	ghost.dispose();
	playerColShape.dispose();
    }

    // =====================================================================================================//
    //
    //
    // CAMERA
    //
    //
    // =====================================================================================================//
    public PerspectiveCamera camera;

    public final Vector3 position = new Vector3();
    public final Vector3 direction = new Vector3(0, 1, 0);
    public final Vector3 right = new Vector3(0, 0, 0);

    public int screenWidth = 0;
    public int screenHeight = 0;

    protected float dist = -4f;
    protected float angleAroundPlayer;

    public void setCamDistance(int amount) {
	dist += amount;
    }

    /**
     * initializes camera with a given camera
     * 
     * @param camera
     */
    public void initCamera(PerspectiveCamera camera) {
	if (camera == null || currentCameraMode == CAMERA_MODE_NONE)
	    return;
	this.camera = camera;
    }

    /**
     * Initializes a new camera by given screen width and height
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public void initCamera(int screenWidth, int screenHeight) {
	if (currentCameraMode == CAMERA_MODE_NONE)
	    return;
	this.screenWidth = screenWidth;
	this.screenHeight = screenHeight;
	camera = new PerspectiveCamera(75.0f, screenWidth, screenHeight);

	if (currentCameraMode == CAMERA_MODE_FIRST) {
	    camera.position.set(this.position.x, this.position.y, this.position.z);
	    camera.position.rotate(Vector3.Y, -45);
	    camera.lookAt(Vector3.Z);
	}
	if (currentCameraMode == CAMERA_MODE_THIRD) {
	    camera.position.set(this.position.x, this.position.y + 2, this.position.z);
	    camera.lookAt(this.position.x, this.position.y + 2, this.position.z);
	}

	camera.near = 0.1f;
	camera.far = 400f;
    }
    
    
    public void initialRotate(float delta) {
	
    }

    public void switchCameraMode() {
	if (currentCameraMode == CAMERA_MODE_FIRST)
	    currentCameraMode = CAMERA_MODE_THIRD;
	else if (currentCameraMode == CAMERA_MODE_THIRD)
	    currentCameraMode = CAMERA_MODE_FIRST;
//	System.out.println("[player]: switched camera to " + currentCameraMode);
    }

    /**
     * Rotates the camera by given mouse delta x and delta y
     * 
     * @param dx
     *            Mouse delta on x axis
     * @param dy
     *            Mouse delta on y axis
     * @param delta
     *            delta time
     */
    public void rotateCamera(float dx, float dy, float delta) {
	float angley = -dy * delta * 5;
	float anglex = -dx * delta * 5;
	if ((angley > 0 && camera.direction.y > 0.75f) || (angley < 0 && camera.direction.y < -0.75f))
	    angley = 0; // limit y-angle

	right.set(camera.direction).crs(camera.up).nor();

	if (currentCameraMode == CAMERA_MODE_FIRST) {
	    camera.rotateAround(camera.position, right, angley);
	    camera.rotate(Vector3.Y, anglex);
	    rotateCharacter(anglex, angley, delta);
	}
	if (currentCameraMode == CAMERA_MODE_THIRD) {
	    angleAroundPlayer += anglex;
	    camera.rotateAround(camera.position, right, angley);
	    camera.rotate(Vector3.Y, anglex);
	    rotateCharacter(anglex, angley, delta);
	}

    }

    /**
     * Updates the ghost's world transform and syncs camera position with
     * modelinstance position.
     */
    protected void updateCamera() {
	ghost.getWorldTransform(transform);

	if (camera == null || currentCameraMode == CAMERA_MODE_NONE)
	    return;

	if (currentCameraMode == CAMERA_MODE_FIRST) {
	    camera.position.set(transform.getTranslation(camera.position).add(0, 0, 0));
	}
	if (currentCameraMode == CAMERA_MODE_THIRD) {
	    camera.position.set(transform.getTranslation(camera.position).add(
		    -MathUtils.sin(MathUtils.degreesToRadians * angleAroundPlayer) * dist, 2,
		    -MathUtils.cos(MathUtils.degreesToRadians * angleAroundPlayer) * dist));
	}
	
	position.set(transform.getTranslation(position));
	camera.update();

    }

    // =====================================================================================================//
    //
    //
    // CHARACTER TRANSFORM
    //
    //
    // =====================================================================================================//
    public final Vector3 velocity = new Vector3();
    
    /** translation vector **/
    public final Vector3 transl = new Vector3();

    /** buffer vector for dual translation forward and sideward **/
    public final Vector3 buff = new Vector3();

    /**
     * Rotates the character by a given degrees for angle x and gle y
     * 
     * @param anglex
     *            the degrees for rotation around the x axis
     * @param angley
     *            the degrees for rotation around the y axis
     * @param delta
     *            delta time
     */
    protected void rotateCharacter(float anglex, float angley, float delta) {
	this.transform.rotate(Vector3.Y, anglex);
	this.ghost.setWorldTransform(transform);
    }
    
    public void addForce(Vector3 forceDir, float strength) {
	strength = strength / 100 * velocity.z;
	charControl.setWalkDirection(forceDir.nor().scl(strength));
    }

    /**
     * Updates the ghost's control motion values using forwardMove backMove
     * leftMove rightMove and jumpMove. Uses the direction vector of the camera
     * to move forward. Adds a second copy of the direction to the translated
     * vector to enable walking forward/backward in combination with left/right.
     * 
     * @param delta
     *            delta time
     */
    protected void updateMotion(float delta) {
	isMoving = forwardMove || backMove || leftMove || rightMove;

	// skip unwanted
	if (leftMove && rightMove)
	    return;
	if (backMove && forwardMove)
	    return;

	// set current translation vector
	transl.set(camera.direction.nor());

	// prepare second buffer vector
	if (leftMove || rightMove)
	    buff.set(transl);

	transl.y = 0f; // we can't fly...

	if (leftMove)
	    transl.rotate(Vector3.Y, 90);

	if (rightMove)
	    transl.rotate(Vector3.Y, -90);

	if (backMove && !leftMove && !rightMove)
	    transl.set(transl.x * -1, transl.y * -1, transl.z * -1);

	if (forwardMove && (leftMove || rightMove)) {
	    transl.add(buff);
	}

	if (backMove && (leftMove || rightMove)) {
	    buff.set(buff.x * -1, buff.y * -1, buff.z * -1);
	    transl.add(buff);
	}

	if (jumpMove) {
	    charControl.jump();
	    jumpMove = false;
	    if (charControl.canJump()) {
		// playSound(SOUNDS_JUMP);
	    }
	}

	if (isMoving) // finally apply
	{

	    else if (leftMove || rightMove)
		transl.limit(5f);
	    else
		transl.limit(0.005f);
	    
	    velocity.add(transl);
	    if (shiftDown)
		velocity.limit2(0.5f);
	    else
		velocity.limit2(5);
	    charControl.setWalkDirection(velocity);
	   // charControl.setLinearVelocity(velocity.add(transl));
	} else {
	    //charControl.setWalkDirection(Vector3.Zero);
	    // stopSound(SOUNDS_WALK);
	}
    }

    // =====================================================================================================//
    //
    //
    // INPUT LOGIC
    //
    //
    // =====================================================================================================//

    public boolean hitsGround;
    public boolean isMoving;
    protected boolean shiftDown;

    protected boolean leftMove;
    protected boolean rightMove;
    protected boolean forwardMove;
    protected boolean backMove;
    protected boolean jumpMove;

    public void setShiftDown(boolean t) {
	if (shiftDown && t)
	    shiftDown = false;
	shiftDown = t;
    }

    /**
     * Activates forward moving, used by the updatemotion function
     * 
     * @param t
     *            true if moving forward
     */
    public void setMoveForward(boolean t) {
	if (backMove && t)
	    backMove = false;
	forwardMove = t;
    }

    /**
     * Activates back moving, used by the updatemotion function
     * 
     * @param t
     *            true if moving back
     */
    public void setMoveBack(boolean t) {
	if (forwardMove && t)
	    forwardMove = false;
	backMove = t;
    }

    /**
     * Activates left moving, used by the updatemotion function
     * 
     * @param t
     *            true if moving left initialized
     */
    public void setLeftMove(boolean t) {
	if (rightMove && t)
	    rightMove = false;
	leftMove = t;
    }

    /**
     * Activates right moving, used by the updatemotion function
     * 
     * @param t
     *            true if moving right initialized
     */
    public void setRightMove(boolean t) {
	if (leftMove && t)
	    leftMove = false;
	rightMove = t;
    }

    /**
     * Activates jumping, used by the updatemotion function
     * 
     * @param t
     *            true if jump initialized
     */
    public void setJumpMove(boolean t) {
	// if (!hitsGround)return;
	jumpMove = t;
    }

    // =====================================================================================================//
    //
    //
    // SOUND LOGIC
    //
    //
    // =====================================================================================================//
    public final String SOUNDS_SHOOT = "shot";
    public final String SOUNDS_WALK = "walk";
    public final String SOUNDS_JUMP = "jump";

    protected HashMap<String, Sound> sounds;

    protected boolean soundWalkIsPlaying = false;

    public void playSound(String key) {
	if (key.equals(SOUNDS_SHOOT)) {
	    key = key.concat("1"); // get number of weapon here
	    sounds.get(key).play(1.0f);
	    return;
	}

	else if (key.equals(SOUNDS_WALK)) {
	    Sound walk = sounds.get(SOUNDS_WALK);
	    soundWalkIsPlaying = true;
	    walk.setLooping(walk.play(), true);
	    return;
	} else {
	    Sound s = sounds.get(key);
	    if (s != null)
		s.play();
	}

    }

    public void stopSound(String key) {

	if (key.equals(SOUNDS_WALK)) {
	    Sound walk = sounds.get(SOUNDS_WALK);
	    soundWalkIsPlaying = false;
	    walk.stop();

	}
    }

    // =====================================================================================================//
    //
    //
    // WEAPONS AND ITEMS
    //
    //
    // =====================================================================================================//
    protected WeaponStates weapons;
    protected WeaponState currentWeaponState;

    private boolean isShooting;

    public void addWeapon(WeaponState wState) {
	weapons.add(wState.id, wState);
	if (weapons.size() == 1)
	    currentWeaponState = wState;
	notifyListeners();
    }

    public boolean shootWeapon() {
	if (currentWeaponState == null || currentWeaponState.ammo <= 0) {
	    // playSound("empty");
	    return false;
	}

	currentWeaponState.ammo--;
	// playSound("shot");
	isShooting = true;
	return true;
    }

    // =====================================================================================================//
    //
    //
    // OBSERVERS OF CHARACTER
    //
    //
    // =====================================================================================================//
    private List<ICharacterListener> listeners = new ArrayList<ICharacterListener>();

    public void addListener(ICharacterListener listener) {
	listeners.add(listener);
    }

    public void notifyListeners() {
	for (ICharacterListener listener : listeners) {
	    listener.characterStateChanged(getCharacterState());
	}
    }

    public CharacterState getCharacterState() {
	return new CharacterState(getShield(), getSpeed());
    }
}
