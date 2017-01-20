package com.jankuester.ggj.twentyseventeen.models.environment;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class SceneObject extends GameModelInstance {

    public SceneObject(Model model) {
	super(model);
	// TODO Auto-generated constructor stub
    }

    public SceneObject(Model model, Vector3 pos) {
	super(model, pos);
	// TODO Auto-generated constructor stub
    }

    public SceneObject(Model model, String rootNode, boolean mergeTransform) {
	super(model, rootNode, mergeTransform);
	// TODO Auto-generated constructor stub
    }

}
