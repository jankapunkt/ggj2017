/**
 * 
 */
package com.jankuester.ggj.twentyseventeen.models.characters;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
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
public class RigidCharacter extends GameModelInstance implements ICollidable{
	
	//vectors
	public Vector3 pos;
	public Vector3 norm;

	protected int jumpVel=0;
	protected final int MAX_JUMP =4; 
	
	//collision
	protected btRigidBody.btRigidBodyConstructionInfo constructionInfo;
	protected btCollisionShape playerColShape;
	protected btRigidBody playerColObj;
	
	protected Vector3 localInertia = new Vector3();
	protected float mass;
	
	protected PlayerMotionState playerMotionstate;
	
	
	//camera and view field
	public PerspectiveCamera camera;
	public int screenWidth=0;
	public int screenHeight=0;
	
	protected float PI = (float)Math.PI;
	
	//input logic
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
	 * @param shape The collisionshape for this body
	 */
	public RigidCharacter(Model model, String id, float x, float y, float z) {
		super(model);
		this.mass = 75;
		this.pos = new Vector3(x,y,z);
		this.transform.setTranslation(this.pos);
		this.norm = new Vector3();
		
		//------- init collision stuff --------//
		playerColShape = new btCapsuleShape(0.5f, 2f);
        if (mass > 0f)
        	playerColShape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);
		
		this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(
				mass, 
				null, 
				playerColShape, 
				localInertia);
		this.constructionInfo.setRestitution(0);
		
		
		playerMotionstate = new PlayerMotionState();
		playerMotionstate.transform = this.transform;
		
		playerColObj = new btRigidBody(constructionInfo);
		playerColObj.setMotionState(playerMotionstate);
		playerColObj.proceedToTransform(transform);
		playerColObj.setUserValue(10101010);
		playerColObj.setCollisionFlags(playerColObj.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
		playerColObj.setSleepingThresholds(0, 0);
		playerColObj.setAngularFactor(0);
		playerColObj.setDamping(0, 0);
	}


	public void dispose()
	{
		playerColObj.dispose();
		playerColShape.dispose();
		constructionInfo.dispose();
		playerMotionstate.dispose();
	}
	
	
	public void initCamera(PerspectiveCamera camera)
	{
		this.camera = camera;
	}

	public void initCamera(int screenWidth, int screenHeight) {
			this.screenWidth = screenWidth;
			this.screenHeight= screenHeight;
			camera = new PerspectiveCamera(75.0f,screenWidth,screenHeight);
			camera.position.set(this.pos.x,this.pos.y,this.pos.z);
			camera.position.rotate(Vector3.Y, -45);
			camera.lookAt(Vector3.Z);
			camera.near = 0.1f;
			camera.far  = 600f;
	}

	public void render(ModelBatch modelBatch, Environment env) {
		modelBatch.render(this, env);
	}
	
	public void updateBeforeRender(float delta) {
		updateMotion(delta);
		updateCamera();
	}
	
	
	/** syncs cam pos with player pos **/
	protected void updateCamera()
	{
		camera.position.set(transform.getTranslation(camera.position).add(0, 1, 0));
		camera.update();
	}
	
	public void rotateCamera(float dx, float dy, float delta)
	{
		Vector3 right = new Vector3().set(camera.direction).crs(camera.up).nor();
		float angley = -dy * delta * 5;
		float anglex = -dx * delta * 5;
		if ((angley > 0 && camera.direction.y > 0.75f) ||
			(angley < 0 && camera.direction.y < -0.75f))
			 angley = 0; //limit y-angle
		camera.rotateAround(camera.position, right, angley);
		camera.rotate(Vector3.Y, anglex);
		
		this.transform.rotate(Vector3.Y, anglex);
		
		Matrix4 trans = this.playerColObj.getCenterOfMassTransform();
		trans.rotate(Vector3.Y, anglex);
		
		this.playerColObj.setCenterOfMassTransform(trans);;
		
		
	}
	
	
	/** updates body velocity **/
    public void updateMotion(float delta)
    {
    	playerColObj.activate();
    	//if (!hitsGround)
    	this.transform.translate(0, -0.2f, 0);
    	
    	isMoving = forwardMove || backMove || leftMove || rightMove;
    	
    	//skip bullshit
    	if (leftMove && rightMove)return;
    	if (backMove && forwardMove)return;
    	
    	Vector3 transl = new Vector3().set(camera.direction.nor());
    	transl.y = 0f;
    	Vector3 buff = transl.cpy();
    	
        if (leftMove)
        	transl.rotate(Vector3.Y, 90);
        
        if (rightMove)
        	transl.rotate(Vector3.Y, -90);
        
        if (backMove && !leftMove && !rightMove)
        	transl.set(transl.x*-1, transl.y*-1, transl.z*-1);
        
        if (forwardMove && (leftMove || rightMove))
        	transl.add(buff);
        
        if (backMove && (leftMove || rightMove))
        {
        	buff.set(buff.x*-1, buff.y*-1, buff.z*-1);
        	transl.add(buff);
        }

        if (jumpMove)
        {
        	playerColObj.applyCentralForce(new Vector3(0,(MAX_JUMP-jumpVel)*3500,0));
        	jumpVel++;
        	if (jumpVel>=MAX_JUMP)
        	{
        		jumpVel = 0;
        		jumpMove = false;
        	}else
        		return;
        }
        
        if (isMoving)
        {
        	if (hitsGround)
        	{
        		transl.scl(15000*delta);
        		transl.limit(20);
        		playerColObj.setLinearVelocity(transl);
        	}
        	else
        	{
        		transl.scl(15000*delta);
        		transl.limit(10);
        		
        	}
        	
        }
       
    }
	
    public void setMoveForward(boolean t)
    {
        if(backMove && t) backMove = false;
        forwardMove = t;
    }
    
    public void setMoveBack(boolean t)
    {
        if(forwardMove && t) forwardMove = false;
        backMove = t;
    }
	
    public void setLeftMove(boolean t)
    {
        if(rightMove && t) rightMove = false;
        leftMove = t;
    }
    public void setRightMove(boolean t)
    {
        if(leftMove && t) leftMove = false;
        rightMove = t;
    }
    
    public void setJumpMove(boolean t)
    {
    	if (!hitsGround)return;
    	jumpMove = t;
    }


	public btRigidBody getBody()
	{
		return this.playerColObj;
	}
	
	public void setContactInfo(btRigidBody.btRigidBodyConstructionInfo contactInfo)
	{
		this.constructionInfo = contactInfo;
	}
	
	
	
	/** internal motionstate class **/
	static class PlayerMotionState extends btMotionState
	{
        public Matrix4 transform;
        @Override
        public void getWorldTransform (Matrix4 worldTrans) {
            worldTrans.set(transform);
        }
        @Override
        public void setWorldTransform (Matrix4 worldTrans) {
        	transform.set(worldTrans);
        }
	}



	public void setCamDistance(int amount) {
		System.out.println(amount);
	}

}
