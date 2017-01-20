package com.jankuester.ggj.twentyseventeen.models.managers;

import java.util.ArrayList;
import java.util.List;

import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.environment.SceneObject;

public class SceneObjectsManager implements IModelInstanceManager{

    private final ArrayList<SceneObject> instances = new ArrayList<SceneObject>();
    
    public SceneObjectsManager() {
	// TODO Auto-generated constructor stub
    }

    public List<? extends GameModelInstance> getRenderingInstances() {
	return this.instances;
    }
}
