package com.jankuester.ggj.twentyseventeen.models.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.jankuester.ggj.twentyseventeen.bullet.CollisionDefs;
import com.jankuester.ggj.twentyseventeen.bullet.DefaultMotionState;
import com.jankuester.ggj.twentyseventeen.bullet.ICollidable;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class RaceCourseObject extends GameModelInstance implements ICollidable {

    private final BoundingBox bounds = new BoundingBox();
    private final DefaultMotionState motionState;
    // collision
    public btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    public btCollisionShape colShape;
    public btRigidBody colBody;
    public Vector3 localInertia;

    public RaceCourseObject(Model model, Vector3 position, btRigidBody.btRigidBodyConstructionInfo bodyConstructionInfo) {
	super(model, position);
	calculateBoundingBox(bounds);
	System.out.println("create new map part");
	motionState = new DefaultMotionState();
	motionState.transform = transform;
	colShape = new btBoxShape(new Vector3(bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getDepth() / 2));
	colBody = new btRigidBody(bodyConstructionInfo);
	colBody.setMotionState(motionState);
	colBody.setFriction(100);
	this.userData = new Color(position.x, position.y, position.z, 1f);
    }

    public RaceCourseObject(Model model, Vector3 position, float mass) {
	super(model, position);
	calculateBoundingBox(bounds);
	System.out.println("create new map part");
	colShape = new btBoxShape(new Vector3(bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getDepth() / 2));

	motionState = new DefaultMotionState();
	motionState.transform = transform;

	create(position, mass);
    }

    protected void create(Vector3 position, float mass) {
	localInertia = new Vector3();
	if (mass > 0f)
	    colShape.calculateLocalInertia(mass, localInertia);
	else
	    localInertia.set(0, 0, 0);

	constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, colShape, localInertia);
	this.transform.translate(position);

	colBody = new btRigidBody(constructionInfo);
	colBody.setMotionState(motionState);
	colBody.proceedToTransform(this.transform);
	colBody.setUserValue(CollisionDefs.generateUserValue());
	if (mass > 0)
	    colBody.setCollisionFlags(colBody.getCollisionFlags());
	else
	    colBody.setCollisionFlags(colBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);

	this.userData = new Color(position.x, position.y, position.z, 1f);
    }

    public RaceCourseObject(Model model, Vector3 position) {
	super(model);

	// init motionstate
	motionState = new DefaultMotionState();
	motionState.transform = transform;

	colShape = //new btBvhTriangleMeshShape(model.meshParts);
		Bullet.obtainStaticNodeShape(model.nodes);
	create(position, 0);
    }

    public void remove() {

    }

    public void dispose() {
	if (motionState != null)
	    motionState.dispose();
	if (colShape != null)
	    colShape.dispose();
	if (colBody != null)
	    colBody.dispose();
	if (constructionInfo != null)
	    constructionInfo.dispose();
    }

    public btRigidBody getBody() {
	return this.colBody;
    }

    public void setContactInfo(btRigidBody.btRigidBodyConstructionInfo contactInfo) {
	this.constructionInfo = contactInfo;
    }

}
