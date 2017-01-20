package com.jankuester.ggj.twentyseventeen.models.factories;

import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;

public class AttributeFactory {
    
    public static PointLightsAttribute getPointLightAttribute(PointLight light) {
	PointLightsAttribute pointLightAttribute = new PointLightsAttribute();
	pointLightAttribute.lights.add(light);
	return pointLightAttribute;
    }
}
