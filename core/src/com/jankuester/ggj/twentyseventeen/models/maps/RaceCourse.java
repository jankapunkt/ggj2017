package com.jankuester.ggj.twentyseventeen.models.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.factories.ModelFactory;
import com.jankuester.ggj.twentyseventeen.models.managers.BaseModelInstanceManager;
import com.jankuester.ggj.twentyseventeen.models.managers.IModelInstanceManager;

public class RaceCourse extends BaseModelInstanceManager implements IModelInstanceManager{

    public final ArrayList<RaceCourseObject> instances = new ArrayList<RaceCourseObject>();

    public RaceCourse() {}

    public RaceCourseObject create(String g3dbPath) {
	return createMapObjectFromG3DBFile(g3dbPath, 0, 0, 0, 0);
    }

    public RaceCourseObject create(String g3dbPath, float mass) {
	return createMapObjectFromG3DBFile(g3dbPath, 0, 0, 0, mass);
    }

    public RaceCourseObject create(String g3dbPath, float x, float y, float z, float mass) {
	return createMapObjectFromG3DBFile(g3dbPath, x, y, z, mass);
    }

    public RaceCourseObject createTerrain(String path) {
	Model currentMap = ModelFactory.getG3DBModel(path);
	return createTerrain(currentMap, path);
    }

    public RaceCourseObject createTerrain(Model model, String id) {
	RaceCourseObject obj = new RaceCourseObject(model, new Vector3(0, 0, 0));
	obj.setId(id);
	return obj;
    }

    public RaceCourseObject createTerrain(String id, float width, float height, float depth, Vector3 pos, Color col, final Attribute... attribute) {
	Model model = models.get(id);
	if (model == null)
	    model = ModelFactory.getBox(width, height, depth, col, attribute);
	return createTerrainWithCollisionBody(model, id, pos);
    }
    
    public RaceCourseObject createTerrainWithCollisionBody(Model model, String id, Vector3 pos) {
	models.put(id, model);
	RaceCourseObject currentMapInstance = new RaceCourseObject(model, pos);
	currentMapInstance.setId(id + "_"+Integer.toString(instances.size()));
	bodyConstructionInfo.put(id, currentMapInstance.constructionInfo);
	instances.add(currentMapInstance);
	System.out.println("[Map]: models=" + models.size() + " instances=" + instances.size() + " rigidbodyInfos="
		+ bodyConstructionInfo.size());
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
    
    public RaceCourseObject createObstacle(String id, Vector3 dimensions, Vector3 position, float mass, final Attribute... attribute) {
	Model currentModel = models.get(id);
	if (currentModel == null)
	    currentModel = ModelFactory.getBox(dimensions.x, dimensions.y, dimensions.z, Color.GREEN, attribute);
	return createCourseObjectWithCollisionBody(id, position.x, position.y, position.z, mass, currentModel);
    }

    private RaceCourseObject createCourseObjectWithCollisionBody(String id, float x, float y, float z, float mass, Model model) {
	RaceCourseObject currentMapInstance = null;
	btRigidBody.btRigidBodyConstructionInfo currentInfo = bodyConstructionInfo.get(id);
	if (currentInfo == null) {
	    currentMapInstance = new RaceCourseObject(model, new Vector3(x, y, z), mass);
	    bodyConstructionInfo.put(id, currentMapInstance.constructionInfo);
	} else {
	    currentMapInstance = new RaceCourseObject(model, new Vector3(x, y, z), currentInfo);
	}
	currentMapInstance.setId(id + "_"+Integer.toString(instances.size()));
	instances.add(currentMapInstance);
	System.out.println("[Map]: models=" + models.size() + " instances=" + instances.size() + " rigidbodyInfos="+ bodyConstructionInfo.size());
	return currentMapInstance;
    }


    
    public RaceCourseObject getGround() {
	if (instances == null || instances.size() == 0)
	    return null;
	return instances.get(0);
    }
    
    public void update(Vector3 playerPos, float playerSpeed) {
	
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


}
