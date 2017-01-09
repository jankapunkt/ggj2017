package com.jankuester.ggj.twentyseventeen.models.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Disposable;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.characters.KinematicCharacter;

public abstract class Item extends GameModelInstance implements Disposable {

    public btCollisionObject colBody;
    public btCollisionShape colShape;

    public boolean active = true;

    public int returnAfter = -1;
    
    protected float rot = 0f;
    protected String id;
    protected Sound collectItemSound;

    public Item(Model model, float x, float y, float z, String id, String pathToCollectSound) {
	super(model);
	this.id = id;
	transform.translate(x, y, z);
	
	colShape = new btSphereShape(1);
	colBody = new btCollisionObject();
	colBody.setCollisionShape(colShape);
	
	// btRigidBody(0, null, colShape, localInertia);
	colBody.setWorldTransform(transform);
	collectItemSound = Gdx.audio.newSound(Gdx.files.internal(pathToCollectSound));
	this.userData = new Color(x, y, z, 1f);
    }

    public void update(float deltaTime) {
	if (!active)
	    return;
	rot = rot + deltaTime * 0.3f;
	
	//TODO rotate around own axis
	transform.rotate(Vector3.Y, rot);
    }

    public void render(ModelBatch batch, Environment env, Shader shader) {
	if (!active)
	    return;
	if (shader != null)
	    batch.render(this, env, shader);
	else
	    batch.render(this, env);
    }

    public void onCollect(KinematicCharacter collector) {
	throw new Error("Invalid call to an abstract class method. You need to override this method");
    }

    public void dispose() {
	colBody.dispose();
	colShape.dispose();
	model.dispose();
    }

    public btCollisionObject getBody() {
	return colBody;
    }

}
