package com.jankuester.ggj.twentyseventeen.models.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Queue;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.factories.ModelFactory;
import com.jankuester.ggj.twentyseventeen.models.managers.BaseModelInstanceManager;
import com.jankuester.ggj.twentyseventeen.models.managers.IModelInstanceManager;

public class RaceCourse extends BaseModelInstanceManager implements IModelInstanceManager {

    public final ArrayList<RaceCourseObject> instances = new ArrayList<RaceCourseObject>(256);
    public final Queue<Phase> phaseQueue;
    private int mapSize = 64;
    private int currentPlayerPhase = 0;
    private int instanceCount = 0;
    private int phaseCount = 0;

    public RaceCourse(int mapSize) {
	this.mapSize = mapSize;
	phaseQueue = new Queue<Phase>(10);
    }

    public int getMapSize() {
	return mapSize;
    }

    public int getInstanceCount() {
	if (instanceCount == Integer.MAX_VALUE) {
	    instanceCount = 0;
	}
	return instanceCount;
    }

    public int getPhaseCount() {
	if (phaseCount == Integer.MAX_VALUE) {
	    phaseCount = 0;
	}
	return phaseCount;
    }

    public void update(Vector3 playerPos, float playerSpeed) {
	int z_rounded = Math.abs(Math.round(playerPos.z / mapSize));
	if (z_rounded != currentPlayerPhase) {
	    System.out.println("phase: " + z_rounded);
	    currentPlayerPhase = z_rounded;

	}
    }

    public void dispose() {
	Collection<Model> mods = models.values();
	for (Model m : mods)
	    m.dispose();
	for (RaceCourseObject p : instances)
	    p.dispose();
	models.clear();
	instances.clear();
    }

    @Override
    public List<? extends GameModelInstance> getRenderingInstances() {
	return this.instances;
    }

    public void addInstance(GameModelInstance instance) {
	throw new Error("not yet implemented");
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // CREATION OF OBJECTS
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////

    public void addPhase(Phase phase) {
	phaseQueue.addLast(phase);
	instances.addAll(phase.phaseObjects.values());
    }

    public RaceCourseObject createExistingTerrain(String path, Vector3 pos) {
	Model currentMap = ModelFactory.getG3DBModel(path);
	if (currentMap == null)
	    return null;
	return createTerrainWithCollisionBody(currentMap, path, pos);
    }

    public RaceCourseObject createTerrain(String id, Vector3 dimensions, Vector3 pos, Color col,
	    Attributes attributes) {
	Model currentModel = models.get(id);
	if (currentModel == null) {
	    currentModel = ModelFactory.getBox(dimensions.x, dimensions.y, dimensions.z, Color.GREEN, attributes);
	    models.put(id, currentModel);
	}

	return createTerrainWithCollisionBody(currentModel, id, pos);
    }

    public RaceCourseObject createTerrainWithCollisionBody(Model model, String id, Vector3 pos) {
	RaceCourseObject currentMapInstance;
	btRigidBody.btRigidBodyConstructionInfo currentInfo = bodyConstructionInfo.get(id);
	if (currentInfo != null) {
	    currentMapInstance = new RaceCourseObject(model, pos, currentInfo);
	} else {
	    currentMapInstance = new RaceCourseObject(model, pos, 0);
	    bodyConstructionInfo.put(id, currentMapInstance.constructionInfo);
	}
	currentMapInstance.setId(id + "_" + Integer.toString(this.getInstanceCount()));

	// instances.add(currentMapInstance);
	// System.out.println("[Map]: models=" + models.size() + " instances=" +
	// instances.size() + " rigidbodyInfos="+ bodyConstructionInfo.size());
	return currentMapInstance;
    }

    protected RaceCourseObject createMapObjectFromG3DBFile(String g3dbPath, float x, float y, float z, float mass) {
	Model currentMap = models.get(g3dbPath);
	if (currentMap == null) {
	    currentMap = ModelFactory.getG3DBModel(g3dbPath);
	    models.put(g3dbPath, currentMap);
	}
	return createCourseObjectWithCollisionBody(g3dbPath, x, y, z, mass, currentMap);
    }

    private RaceCourseObject createCourseObjectWithCollisionBody(String id, float x, float y, float z, float mass,
	    Model model) {
	RaceCourseObject currentMapInstance = null;
	btRigidBody.btRigidBodyConstructionInfo currentInfo = bodyConstructionInfo.get(id);
	if (currentInfo == null) {
	    currentMapInstance = new RaceCourseObject(model, new Vector3(x, y, z), mass);
	    bodyConstructionInfo.put(id, currentMapInstance.constructionInfo);
	} else {
	    currentMapInstance = new RaceCourseObject(model, new Vector3(x, y, z), currentInfo);
	}
	currentMapInstance.setId(id + "_" + Integer.toString(this.getInstanceCount()));
	//instances.add(currentMapInstance);
	//System.out.println("[Map]: models=" + models.size() + " instances=" + instances.size() + " rigidbodyInfos="+ bodyConstructionInfo.size());
	return currentMapInstance;
    }

    public RaceCourseObject createObstacle(String id, Vector3 dimensions, Vector3 position, float mass,
	    Attributes attributes) {
	Model currentModel = models.get(id);
	if (currentModel == null) {
	    currentModel = ModelFactory.getBox(dimensions.x, dimensions.y, dimensions.z, Color.GREEN, attributes);
	    models.put(id, currentModel);
	}
	return createCourseObjectWithCollisionBody(id, position.x, position.y, position.z, mass, currentModel);
    }
}
