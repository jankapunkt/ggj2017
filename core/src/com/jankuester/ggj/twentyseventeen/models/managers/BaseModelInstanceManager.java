package com.jankuester.ggj.twentyseventeen.models.managers;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public abstract class BaseModelInstanceManager implements IModelInstanceManager {

    public final HashMap<String, Model> models = new HashMap<String, Model>();
    public final HashMap<String, btRigidBody.btRigidBodyConstructionInfo> bodyConstructionInfo = new HashMap<String, btRigidBody.btRigidBodyConstructionInfo>();

    public BaseModelInstanceManager() {

    }

    public List<? extends GameModelInstance> getRenderingInstances() {
	throw new Error("abstract class");
    }
}
