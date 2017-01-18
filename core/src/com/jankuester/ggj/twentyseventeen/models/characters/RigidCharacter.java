/**
 * 
 */
package com.jankuester.ggj.twentyseventeen.models.characters;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.jankuester.ggj.twentyseventeen.bullet.ICollidable;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

/**
 * @author major
 *
 */
public class RigidCharacter extends GameModelInstance implements ICollidable {

    // vectors
    public final Vector3 pos = new Vector3();
    public Vector3 norm;

    protected int jumpVel = 0;
    protected final int MAX_JUMP = 4;

    // collision
    protected btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    protected btCollisionShape playerColShape;
    protected btRigidBody playerColObj;

    protected Vector3 localInertia = new Vector3();

    protected float mass;

    protected PlayerMotionState playerMotionstate;

    // camera and view field
    public PerspectiveCamera camera;
    public int screenWidth = 0;
    public int screenHeight = 0;

    protected float PI = (float) Math.PI;

    // input logic
    protected boolean leftMove;
    protected boolean rightMove;
    protected boolean forwardMove;
    protected boolean backMove;
    protected boolean jumpMove;

    public boolean hitsGround;
    public boolean isMoving;

    /**
     * @param model
     * @param x
     * @param y
     * @param z
     * @param shape
     *            The collisionshape for this body
     */
    public RigidCharacter(Model model, String id, float x, float y, float z) {
	super(model);
	this.mass = 5;
	this.pos.set(x, y, z);
	this.transform.setTranslation(this.pos);
	this.norm = new Vector3();

	// ------- init collision stuff --------//
	playerColShape = new btCapsuleShape(0.5f, 2f);
	if (mass > 0f)
	    playerColShape.calculateLocalInertia(mass, localInertia);
	else
	    localInertia.set(0, 0, 0);

	this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, playerColShape, localInertia);
	this.constructionInfo.setRestitution(0);

	playerMotionstate = new PlayerMotionState();
	playerMotionstate.transform = this.transform;

	playerColObj = new btRigidBody(constructionInfo);
	playerColObj.setMotionState(playerMotionstate);
	playerColObj.proceedToTransform(transform);
	playerColObj.setUserValue(10101010);
	playerColObj.setCollisionFlags(
		playerColObj.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
	//playerColObj.setSleepingThresholds(0, 0);
	playerColObj.setFriction(0);
	
	//playerColObj.setAngularFactor(0);
	//playerColObj.setDamping(0, 0);
    }

    public void dispose() {
	playerColObj.dispose();
	playerColShape.dispose();
	constructionInfo.dispose();
	playerMotionstate.dispose();
    }

    public void initCamera(PerspectiveCamera camera) {
	this.camera = camera;
    }

    public void initCamera(int screenWidth, int screenHeight) {

	this.screenWidth = screenWidth;
	this.screenHeight = screenHeight;
	camera = new PerspectiveCamera(75.0f, screenWidth, screenHeight);

	camera.position.set(this.pos.x, this.pos.y + 2, this.pos.z);
	camera.lookAt(this.pos.x, this.pos.y + 2, this.pos.z);

	camera.near = 0.1f;
	camera.far = 400f;
    }

    public void render(ModelBatch modelBatch, Environment env) {
	modelBatch.render(this, env);
    }

    public void update(float delta) {
	updateMotion(delta);
	updateCamera();
    }

    public final Vector3 direction = new Vector3(0, 1, 0);
    public final Vector3 right = new Vector3(0, 0, 0);

    protected float dist = -4f;
    protected float angleAroundPlayer = 0;

    /** syncs cam pos with player pos **/
    protected void updateCamera() {
	camera.position.set(transform.getTranslation(camera.position).add(0, 1, 0));
	camera.position.set(transform.getTranslation(camera.position).add(
		-MathUtils.sin(MathUtils.degreesToRadians * angleAroundPlayer) * dist, 2,
		-MathUtils.cos(MathUtils.degreesToRadians * angleAroundPlayer) * dist));
	camera.update();
    }

    public void rotateCamera(float dx, float dy, float delta) {
	float angley = -dy * delta * 5;
	float anglex = -dx * delta * 5;
	if ((angley > 0 && camera.direction.y > 0.75f) || (angley < 0 && camera.direction.y < -0.75f))
	    angley = 0; // limit y-angle

	right.set(camera.direction).crs(camera.up).nor();

	angleAroundPlayer += anglex;
	camera.rotateAround(camera.position, right, angley);
	camera.rotate(Vector3.Y, anglex);
	rotateCharacter(anglex, angley, delta);

    }

    protected void rotateCharacter(float anglex, float angley, float delta) {
	this.transform.rotate(Vector3.Y, anglex);
	this.getBody().setWorldTransform(transform);
    }

    public final Vector3 transl = new Vector3();
    public final Vector3 buff = new Vector3();
    public final Vector3 vel = new Vector3();
    

    /** updates body velocity **/
    public void updateMotion(float delta) {
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

	if (isMoving) // finally apply
	{
	    playerColObj.setLinearVelocity(vel.add(transl));
	  
	} else {
	    // charControl.setWalkDirection(Vector3.Zero);
	    // stopSound(SOUNDS_WALK);
	}
    }

    public void setMoveForward(boolean t) {
	if (backMove && t)
	    backMove = false;
	forwardMove = t;
    }

    public void setMoveBack(boolean t) {
	if (forwardMove && t)
	    forwardMove = false;
	backMove = t;
    }

    public void setLeftMove(boolean t) {
	if (rightMove && t)
	    rightMove = false;
	leftMove = t;
    }

    public void setRightMove(boolean t) {
	if (leftMove && t)
	    leftMove = false;
	rightMove = t;
    }

    public void setJumpMove(boolean t) {
	if (!hitsGround)
	    return;
	jumpMove = t;
    }

    public btRigidBody getBody() {
	return this.playerColObj;
    }

    public void setContactInfo(btRigidBody.btRigidBodyConstructionInfo contactInfo) {
	this.constructionInfo = contactInfo;
    }

    /** internal motionstate class **/
    static class PlayerMotionState extends btMotionState {
	public Matrix4 transform;

	@Override
	public void getWorldTransform(Matrix4 worldTrans) {
	    worldTrans.set(transform);
	}

	@Override
	public void setWorldTransform(Matrix4 worldTrans) {
	    transform.set(worldTrans);
	}
    }

    public void setCamDistance(int amount) {
	System.out.println(amount);
    }

}
