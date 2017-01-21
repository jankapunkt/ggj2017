package com.jankuester.ggj.twentyseventeen.models.managers;

import java.util.ArrayList;
import java.util.List;

import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.environment.SceneObject;
import com.jankuester.ggj.twentyseventeen.models.environment.Sun;

public class SceneObjectsManager implements IModelInstanceManager{

    private final ArrayList<GameModelInstance> instances = new ArrayList<GameModelInstance>();
    
    public SceneObjectsManager() {
	// TODO Auto-generated constructor stub
    }

    public List<? extends GameModelInstance> getRenderingInstances() {
	return this.instances;
    }

    public void addInstance(GameModelInstance instance) {
	instances.add(instance);
    }
}
