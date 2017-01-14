package com.jankuester.ggj.twentyseventeen.models.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class Sun extends GameModelInstance {

    private PointLight light;
    public Vector3 position;

    public static Sun createSun(float x, float y, float z, Color color, float intensity) {
	PointLightsAttribute pointLightAttribute = new PointLightsAttribute();
	PointLight light = createPointLight(x,y,z,color,intensity);
	pointLightAttribute.lights.add(light);
	ModelBuilder mb = new ModelBuilder();
	Model model = mb.createSphere(1, 1, 1, 5, 5,
		new Material(ColorAttribute.createDiffuse(Color.YELLOW), pointLightAttribute),
		Usage.Position | Usage.Normal);
	return new Sun(model, new Vector3(x, y, z), light);
    }
    
    public static PointLight createPointLight(float x, float y, float z, Color color, float intensity) {
	return new PointLight().set(color, new Vector3(x, y, z), intensity);
    }

    public Sun(Model model, Vector3 pos, PointLight light) {
	super(model, pos);
	this.position = new Vector3(pos);
	this.light = light;
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
}
