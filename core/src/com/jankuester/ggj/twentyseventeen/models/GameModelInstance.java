package com.jankuester.ggj.twentyseventeen.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class GameModelInstance extends ModelInstance {

    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();

    private boolean isSpheric = true;

    public final float radius;

    private final static BoundingBox bounds = new BoundingBox();
    
    private String id="gameModelInstance_unsetId";

    public GameModelInstance(Model model) {
	super(model);
	calculateBounds();
	radius = dimensions.len() / 2f;
    }

    public GameModelInstance(Model model, Vector3 pos) {
	super(model, pos);
	calculateBounds();
	radius = dimensions.len() / 2f;
    }

    public GameModelInstance(Model model, String rootNode, boolean mergeTransform) {
	super(model, rootNode, mergeTransform);
	calculateBounds();
	radius = dimensions.len() / 2f;
    }

    private void calculateBounds() {
	calculateBoundingBox(bounds);
	bounds.getCenter(center);
	bounds.getDimensions(dimensions);
    }

    public BoundingBox getBounds() {
	return bounds;
    }

    public boolean isSpheric() {
	return isSpheric;
    }

    public void setSpheric(boolean isSpheric) {
	this.isSpheric = isSpheric;
    }

    public String getId() {
	return id;
    }
    
    public void setId(String id) {
	this.id = id;
    }
}
