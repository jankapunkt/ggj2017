package com.jankuester.ggj.twentyseventeen.models.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Queue;
import com.jankuester.ggj.twentyseventeen.bullet.CollisionDefs;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.factories.AttributeFactory;
import com.jankuester.ggj.twentyseventeen.models.factories.MaterialFactory;
import com.jankuester.ggj.twentyseventeen.models.factories.ModelFactory;
import com.jankuester.ggj.twentyseventeen.models.managers.BaseModelInstanceManager;
import com.jankuester.ggj.twentyseventeen.models.managers.IModelInstanceManager;
import com.jankuester.ggj.twentyseventeen.models.managers.IPhaseUpdateListener;
import com.jankuester.ggj.twentyseventeen.system.Utils;

public class RaceCourse extends BaseModelInstanceManager implements IModelInstanceManager {

    public final ArrayList<RaceCourseObject> instances = new ArrayList<RaceCourseObject>(256);
    public final Queue<Phase> phaseQueue;
    private int mapSize = 64;
    private float halfMapSize;
    private float quarterMapSize;
    private float octerMapSize;
    private int currentPlayerPhase = 0;
    private int instanceCount = 0;
    private int phaseCount = 0;
    private int nextPhaseType;

    private final btDynamicsWorld dynamicsWorldRef;
    private int random;

    public RaceCourse(int mapSize, final btDynamicsWorld dynamicsWorlfRef) {
	this.mapSize = mapSize;
	halfMapSize = mapSize / 2;
	quarterMapSize = mapSize / 4;
	octerMapSize = mapSize / 8;
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
	    currentPlayerPhase = z_rounded;
	    if (phaseQueue.size > 0) {
		Phase currentPhase = phaseQueue.first();
		//currentPhase.update();
		if (z_rounded == currentPhase.phaseId)
		    ;
		notifyPhaseUpdate(z_rounded, currentPhase.phaseType);
		if (z_rounded > currentPhase.phaseId + 1)
		    disposePhase(phaseQueue.removeFirst());
	    }

	    if (z_rounded % 10 == 0) {
		nextPhaseType = Utils.random(0, 3);
	    }
	    createPhase(phaseCount, nextPhaseType);

	}
    }

    public void createPhase(int index, int phaseType) {
	Phase phase = new Phase(this.getPhaseCount(), phaseType);
	String phasename = Phase.getPhasename(phaseType);

	System.out.println("create phase: " + index + " => " + phaseType + " => " + phasename);

	Color phaseColor = Phase.getPhaseColor(phaseType, false);
	Color complementaryColor = Phase.getPhaseColor(phaseType, true);
	phaseColor.a = 0.5f;
	PointLight pl = ModelFactory.createPointLight(0, 52, index * mapSize, Color.WHITE, 8000f);
	Attributes attributes = new Attributes();
	attributes.set(ColorAttribute.createDiffuse(phaseColor), ColorAttribute.createSpecular(Color.WHITE),
		AttributeFactory.getPointLightAttribute(pl));

	RaceCourseObject groundModel = this.createTerrain("ground", phasename, new Vector3(mapSize, 1f, mapSize),
		new Vector3(0, 0, index * mapSize), MaterialFactory.createMaterial(phasename, attributes));
	btRigidBody groundBody = groundModel.getBody();
	groundBody.setContactCallbackFlag(CollisionDefs.GROUND_FLAG);
	groundBody.setFriction(0);
	groundBody.setContactCallbackFilter(0);
	dynamicsWorldRef.addRigidBody(groundBody);
	phase.courseObjects.put(groundModel.getId(), groundModel);

	Vector3 wallSize = new Vector3(2f, 5f, mapSize);

	// WALL LEFT

	RaceCourseObject wallLeftModel = this.createTerrain("wall_left", phasename, wallSize,
		new Vector3(-mapSize / 2 - 1, 2, index * mapSize),
		MaterialFactory.createMaterial(phasename, attributes));
	btRigidBody wallLeftBody = wallLeftModel.getBody();
	wallLeftBody.setFriction(0);
	wallLeftBody.setDamping(0, 0);
	wallLeftBody.setAnisotropicFriction(Vector3.Zero);
	wallLeftBody.setContactCallbackFlag(CollisionDefs.GROUND_FLAG | CollisionDefs.WALL_LEFT);
	wallLeftBody.setContactCallbackFilter(0);
	dynamicsWorldRef.addRigidBody(wallLeftBody);
	phase.courseObjects.put(wallLeftModel.getId(), wallLeftModel);

	// WALL RIGHT

	RaceCourseObject wallRightModel = this.createTerrain("wallRight", phasename, wallSize,
		new Vector3(mapSize / 2 + 1, 2, index * mapSize),
		MaterialFactory.createMaterial(phasename, attributes));
	btRigidBody wallRightBody = wallRightModel.getBody();
	wallRightBody.setContactCallbackFlag(CollisionDefs.GROUND_FLAG | CollisionDefs.WALL_RIGHT);
	wallRightBody.setContactCallbackFilter(0);
	dynamicsWorldRef.addRigidBody(wallRightBody);
	phase.courseObjects.put(wallRightModel.getId(), wallRightModel);

	int chance = 0;
	int lowChance = 0;
	switch (phaseType) {
	case Phase.TYPE_OBSTACLES:
	    chance = 100;
	    lowChance = 30;
	    break;
	case Phase.TYPE_CLEAR:
	    chance = 60;
	    lowChance = 0;
	    break;
	case Phase.TYPE_INNOCENTS:
	    chance = 70;
	    lowChance = 10;
	    break;
	case Phase.TYPE_SPEED:
	    chance = 85;
	    lowChance = 20;
	    break;
	default:
	    break;
	}
	boolean placeObject = Utils.random(lowChance, chance) > 50 ? true : false;
	if (placeObject) {
	    RaceCourseObject boxModel = createObstacle(index, phasename, pl, complementaryColor);
	    phase.courseObjects.put(boxModel.getId(), boxModel);
	}

	addPhase(phase);
	notifyPhaseCreated(phase, phaseType, phasename, index);
    }

    private RaceCourseObject createObstacle(int index, String phasename, PointLight pl, Color col) {
	Attributes obstacleAtt = new Attributes();
	obstacleAtt.set(ColorAttribute.createDiffuse(col), ColorAttribute.createSpecular(Color.WHITE),
		AttributeFactory.getPointLightAttribute(pl));

	int posx = Utils.random(0, mapSize - (int) quarterMapSize);
	int posz = Utils.random(0, mapSize);
	RaceCourseObject boxModel = this.createObstacle("obstacle", phasename, new Vector3(quarterMapSize, 6, 6),
		new Vector3(-halfMapSize + posx, 5, index * mapSize + posz), 500f, obstacleAtt);
	btRigidBody box = boxModel.getBody();
	box.setCollisionFlags(box.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
	box.setContactCallbackFlag(CollisionDefs.OBJECT_FLAG | CollisionDefs.OBSTACLE_FLAG);
	box.setContactCallbackFilter(CollisionDefs.PLAYER_FLAG);
	box.setUserValue(CollisionDefs.generateUserValue());
	dynamicsWorldRef.addRigidBody(box);
	return boxModel;
    }

    public void addPhase(Phase phase) {
	System.out.println("add phase " + phaseCount);
	phaseQueue.addLast(phase);
	instances.addAll(phase.courseObjects.values());
	phaseCount++;
	System.out.println("[Map]: models=" + models.size() + " instances=" + instances.size() + " rigidbodyInfos="
		+ bodyConstructionInfo.size());
    }

    private void disposePhase(Phase phase) {
	Collection<RaceCourseObject> phaseValues = phase.courseObjects.values();
	instances.removeAll(phaseValues);
	for (RaceCourseObject raceCourseObject : phaseValues) {
	    dynamicsWorldRef.removeRigidBody(raceCourseObject.getBody());
	    raceCourseObject.dispose();
	}
	phase.courseObjects.clear();
	phase = null;
    }

    // =====================================================================================================//
    //
    // OBSERVERS OF IPhaseUpdateListener
    //
    // =====================================================================================================//
    // private List<IPhaseUpdateListener> listeners = new
    // ArrayList<IPhaseUpdateListener>();

    IPhaseUpdateListener listener;

    public void addListener(IPhaseUpdateListener listener) {
	this.listener = listener;
    }

    private void notifyPhaseCreated(Phase phase, int phaseType, String phasename, int index) {
	listener.phaseCreated(phase, phaseType, phasename, index);
    }

    private void notifyPhaseUpdate(int z_rounded, int phaseType) {
	listener.phaseStarted(z_rounded, phaseType);
    }

    private void notifyPhaseDispose(Phase phase) {
	listener.phaseDisposed(phase);
    }

    // =====================================================================================================//
    //
    // DISPOSAL
    //
    // =====================================================================================================//

    public void dispose() {
	Collection<Model> mods = models.values();
	for (Model m : mods)
	    m.dispose();
	for (RaceCourseObject p : instances)
	    p.dispose();
	models.clear();
	instances.clear();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    // IModelInstanceManager
    //
    //
    //////////////////////////////////////////////////////////////////////////////////////////

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

    public RaceCourseObject createTerrainFromG3DBModel(String path, Vector3 pos) {
	Model currentMap = ModelFactory.getG3DBModel(path);
	if (currentMap == null)
	    return null;
	return createTerrainWithCollisionBody(currentMap, path, pos);
    }

    public RaceCourseObject createTerrain(String id, String phaseName, Vector3 dimensions, Vector3 pos, Color col,
	    Attributes attributes) {
	String materialKey = id + phaseName;
	Model currentModel = models.get(materialKey);
	if (currentModel == null) {
	    currentModel = ModelFactory.createBoxModel(materialKey, dimensions.x, dimensions.y, dimensions.z,
		    Color.GREEN, attributes);
	    models.put(materialKey, currentModel);
	}

	return createTerrainWithCollisionBody(currentModel, id, pos);
    }

    public RaceCourseObject createTerrain(String id, String phaseName, Vector3 dimensions, Vector3 pos, Material mat) {
	String materialKey = id + phaseName;
	/*
	 * Model currentModel = models.get(materialKey); if (currentModel ==
	 * null) { System.out.println("new terrain model => " + materialKey);
	 * currentModel = ModelFactory.createBoxModel(materialKey, dimensions.x,
	 * dimensions.y, dimensions.z, mat); if (currentModel == null) throw new
	 * Error(); models.put(materialKey, currentModel); }
	 */
	Model currentModel = ModelFactory.createBoxModel(materialKey, dimensions.x, dimensions.y, dimensions.z, mat);
	return createTerrainWithCollisionBody(currentModel, id, pos);
    }

    protected RaceCourseObject createMapObjectFromG3DBFile(String g3dbPath, String phaseName, float x, float y, float z,
	    float mass) {
	String materialKey = g3dbPath + phaseName;
	Model currentMap = models.get(materialKey);
	if (currentMap == null) {
	    currentMap = ModelFactory.getG3DBModel(materialKey);
	    models.put(materialKey, currentMap);
	}
	return createCourseObjectWithCollisionBody(g3dbPath, x, y, z, mass, currentMap);
    }

    public RaceCourseObject createObstacle(String id, String phaseName, Vector3 dimensions, Vector3 position,
	    float mass, Attributes attributes) {
	String materialKey = id + phaseName;
	Model currentModel = ModelFactory.createBoxModel(materialKey, dimensions.x, dimensions.y, dimensions.z,
		Color.GREEN, attributes);

	return createCourseObjectWithCollisionBody(id, position.x, position.y, position.z, mass, currentModel);
    }

    public RaceCourseObject createObstacle(String id, String phaseName, Vector3 dimensions, Vector3 position,
	    float mass, Material mat) {
	String materialKey = id + phaseName;
	Model currentModel = models.get(materialKey);
	if (currentModel == null) {
	    currentModel = ModelFactory.createBoxModel(materialKey, dimensions.x, dimensions.y, dimensions.z, mat);
	    models.put(materialKey, currentModel);
	}
	return createCourseObjectWithCollisionBody(id, position.x, position.y, position.z, mass, currentModel);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
	return currentMapInstance;
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
	return currentMapInstance;
    }
}
