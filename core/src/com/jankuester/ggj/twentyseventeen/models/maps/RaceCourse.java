package com.jankuester.ggj.twentyseventeen.models.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Queue;
import com.jankuester.ggj.twentyseventeen.bullet.CollisionDefs;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.environment.Sun;
import com.jankuester.ggj.twentyseventeen.models.factories.AttributeFactory;
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
    private int currentPhaseType = Phase.TYPE_CLEAR;

    private final btDynamicsWorld dynamicsWorldRef;

    public RaceCourse(int mapSize, final btDynamicsWorld dynamicsWorlfRef) {
	this.mapSize = mapSize;
	phaseQueue = new Queue<Phase>(16);
	this.dynamicsWorldRef = dynamicsWorlfRef;
    }

    public int getMapSize() {
	return mapSize;
    }

    public int getInstanceCount() {
	if (instanceCount == Integer.MAX_VALUE) {
	    instanceCount = 0;
	}
	return instanceCount++;
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
	    if (phaseQueue.size > 0)
		disposePhase(phaseQueue.removeFirst());
	    createPhase(phaseCount, currentPhaseType);
	}
	if (z_rounded % 5 == 0) {
	    currentPhaseType = (int) Math.round(Math.random() * 3);
	}
    }

    public void createPhase(int index, int phaseType) {
	Phase phase = new Phase(this.getPhaseCount());
	System.out.println("create phase: "+ phaseType);
	Color phaseColor;
	switch (phaseType) {
	case Phase.TYPE_CLEAR:
	    phaseColor = Color.ORANGE;
	    break;
	case Phase.TYPE_INNOCENTS:
	    phaseColor = Color.PINK;
	    break;
	case Phase.TYPE_OBSTACLES:
	    phaseColor = Color.RED;
	    break;
	case Phase.TYPE_SPEED:
	    phaseColor = Color.GREEN;
	    break;
	default:
	    throw new Error("unknown phase");
	}

	PointLight pl = ModelFactory.createPointLight(0, 6, -index * mapSize, Color.WHITE, 5000f);

	Attributes atts = new Attributes();
	atts.set(ColorAttribute.createDiffuse(phaseColor), AttributeFactory.getPointLightAttribute(pl));

	// GROUND
	
	RaceCourseObject groundModel = this.createObstacle("ground", new Vector3(mapSize, 1f, mapSize),
		new Vector3(0, 0, -index * mapSize), 0, atts);
	btRigidBody groundBody = groundModel.getBody();
	groundBody.setContactCallbackFlag(CollisionDefs.GROUND_FLAG);
	groundBody.setFriction(0);
	groundBody.setContactCallbackFilter(0);
	dynamicsWorldRef.addRigidBody(groundBody);
	phase.phaseObjects.put(groundModel.getId(), groundModel);

	// WALL LEFT
	
	RaceCourseObject wallLeftModel = this.createTerrain("wall_left", new Vector3(2f, 5f, mapSize),
		new Vector3(-mapSize / 2 - 1, 0, -index * mapSize), phaseColor, atts);
	btRigidBody wallLeftBody = wallLeftModel.getBody();
	wallLeftBody.setContactCallbackFlag(CollisionDefs.GROUND_FLAG | CollisionDefs.WALL_FLAG);
	wallLeftBody.setContactCallbackFilter(0);
	dynamicsWorldRef.addRigidBody(wallLeftBody);
	phase.phaseObjects.put(wallLeftModel.getId(), wallLeftModel);

	// WALL RIGHT
	
	RaceCourseObject wallRightModel = this.createTerrain("wallRight", new Vector3(2f, 5f, mapSize),
		new Vector3(mapSize / 2 + 1, 0, -index * mapSize), phaseColor, atts);
	btRigidBody wallRightBody = wallRightModel.getBody();
	wallRightBody.setContactCallbackFlag(CollisionDefs.GROUND_FLAG | CollisionDefs.WALL_FLAG);
	wallRightBody.setContactCallbackFilter(0);
	dynamicsWorldRef.addRigidBody(wallRightBody);
	phase.phaseObjects.put(wallRightModel.getId(), wallRightModel);

	addPhase(phase);
    }

    public void addPhase(Phase phase) {
	phaseQueue.addLast(phase);
	instances.addAll(phase.phaseObjects.values());
	phaseCount++;
    }

    private void disposePhase(Phase phase) {
	Collection<RaceCourseObject> phaseValues = phase.phaseObjects.values();
	instances.removeAll(phaseValues);
	for (RaceCourseObject raceCourseObject : phaseValues) {
	    dynamicsWorldRef.removeRigidBody(raceCourseObject.getBody());
	    raceCourseObject.dispose();
	}
	phase.phaseObjects.clear();
	phase = null;
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
	    currentModel = ModelFactory.getBox(id, dimensions.x, dimensions.y, dimensions.z, Color.GREEN, attributes);
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
	// instances.add(currentMapInstance);
	// System.out.println("[Map]: models=" + models.size() + " instances=" +
	// instances.size() + " rigidbodyInfos="+ bodyConstructionInfo.size());
	return currentMapInstance;
    }

    public RaceCourseObject createObstacle(String id, Vector3 dimensions, Vector3 position, float mass,
	    Attributes attributes) {
	Model currentModel = models.get(id);
	if (currentModel == null) {
	    currentModel = ModelFactory.getBox(id, dimensions.x, dimensions.y, dimensions.z, Color.GREEN, attributes);
	    models.put(id, currentModel);
	}
	return createCourseObjectWithCollisionBody(id, position.x, position.y, position.z, mass, currentModel);
    }
}
