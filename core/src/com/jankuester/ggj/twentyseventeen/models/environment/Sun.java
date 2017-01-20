package com.jankuester.ggj.twentyseventeen.models.environment;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class Sun extends GameModelInstance {

    private PointLight light;
    public final Vector3 position;

    public Sun(Model model, Vector3 pos, PointLight light) {
	super(model, pos);
	this.position = new Vector3(pos);
	this.light = light;
	//transform.translate(pos);
	light.position.set(pos);
    }

    public void rotateAround(Vector3 center, Vector3 axis, float angle) {
	Vector3 trans = transform.getTranslation(center);
	// System.out.println(trans.toString());
	float x = trans.x;
	float y = trans.y;
	float z = trans.z;
	transform.translate(-x, -y, -z).rotate(axis, angle).translate(x, y, z);
	light.position.set(-x, -y, -z).rotate(axis, angle).set(x, y, z);
    }

    public PointLight getLight() {
	return light;
    }

    public void update(Vector3 pos) {
	this.transform.translate(pos);
	this.light.position.set(pos);
    }
}
