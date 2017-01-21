package com.jankuester.ggj.twentyseventeen.models.factories;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;

public class MaterialFactory {

    private static final HashMap<String, Material> materials = new HashMap<String, Material>(16);

    public static Material createMaterial(String phaseName, Attributes attributes) {
	Material mat = new Material();
	mat.set(attributes);
	return mat;
    }

    public static Material createMaterial(String id, Color c, Attributes attributes) {
	Material mat = materials.get(id);
	if (mat == null) {
	    mat = new Material(ColorAttribute.createDiffuse(c));
	}
	mat.set(attributes);
	return mat;
    }

    public static Material createMaterial(String id, ColorAttribute ca, PointLightsAttribute pa) {
	Material mat = materials.get(id);
	if (mat == null) {
	    mat = new Material();
	}
	mat.set(ca, pa);
	return mat;
    }

}
