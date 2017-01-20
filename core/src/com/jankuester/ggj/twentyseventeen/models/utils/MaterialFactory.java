package com.jankuester.ggj.twentyseventeen.models.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class MaterialFactory {

    public static Material createMaterial(Color c,  final Attribute... attributes) {
	Material mat = new Material(ColorAttribute.createDiffuse(c));
	mat.set(attributes);
	return mat;
    }

}
