package com.jankuester.ggj.twentyseventeen.graphics.rendering;

import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Vector3;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class EntityRenderingHelper {

    private Camera cam;
    private ModelBatch batch;
    private int count = 0;
    private int last_count = 0;
    public final Vector3 tempPos = new Vector3();

    public EntityRenderingHelper() {
	
    }

    public void setCamera(final Camera cam) {
	this.cam = cam;
    }
    
    public void setModelBatch(final ModelBatch batch) {
	this.batch = batch;
    }
    
    public void begin(final Camera cam, final ModelBatch batch) {
	count=0;
	this.setCamera(cam);
	this.setModelBatch(batch);
	batch.begin(cam);
    }

    public void render(final GameModelInstance instance) {
	if (isVisible(instance)){
	    batch.render(instance);
	    count++;
	}
    }
    
    public void render(final GameModelInstance instance, final Environment environment) {
	if (isVisible(instance)){
	    batch.render(instance, environment);
	    count++;
	}
    }

    public void render(final GameModelInstance instance, final Environment environment, final Shader shader) {
	if (isVisible(instance)){
	    batch.render(instance, environment, shader);
	    count++;
	}
    }
    
    public void render(final List<? extends GameModelInstance> instances) {
	for (GameModelInstance instance : instances) {
	    render(instance);
	}
    }
    
    public void render(final List<? extends GameModelInstance> instances, final Environment environment) {
	for (GameModelInstance instance : instances) {
	    render(instance, environment);
	}
    }

    public void render(final List<? extends GameModelInstance> instances, final Environment environment, final Shader shader) {
	for (GameModelInstance instance : instances) {
	    render(instance, environment, shader);
	}
    }
    
    public void end() {
	if (count != last_count) {
	    //System.out.println("rendered Objects: "+ count);
	    last_count = count;
	}
	
	this.batch.end();
	this.setCamera(null);
	this.setModelBatch(null);
    }
    
    public boolean isVisible(final GameModelInstance instance) {
	if (this.cam == null)
	    return false;
	instance.transform.getTranslation(tempPos);
	
	tempPos.add(instance.center);
	if (tempPos.dst(cam.position) > cam.far)
	    return false;
	if (instance.isSpheric())
	    return cam.frustum.sphereInFrustum(tempPos, instance.radius);
	else
	    return cam.frustum.boundsInFrustum(instance.center, instance.dimensions);
    }
    
}
