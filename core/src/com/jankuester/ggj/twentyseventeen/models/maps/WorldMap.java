package com.jankuester.ggj.twentyseventeen.models.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelFactory;

public class WorldMap {

    public final HashMap<String, Model> models = new HashMap<String, Model>();
    public final HashMap<String, btRigidBody.btRigidBodyConstructionInfo> bodyConstructionInfo = new HashMap<String, btRigidBody.btRigidBodyConstructionInfo>();
    public final ArrayList<MapPart> instances = new ArrayList<MapPart>();

    public WorldMap() {
    }

    public MapPart create(String g3dbPath) {
	return createMapObject(g3dbPath, 0, 0, 0, 0);
    }

    public MapPart create(String g3dbPath, float mass) {
	return createMapObject(g3dbPath, 0, 0, 0, mass);
    }

    public MapPart create(String g3dbPath, float x, float y, float z, float mass) {
	return createMapObject(g3dbPath, x, y, z, mass);
    }

    public MapPart createTerrain(String path) {
	Model currentMap = ModelFactory.getG3DBModel(path);
	return createTerrain(currentMap, path);
    }

    public MapPart createTerrain(Model model, String id) {
	return new MapPart(model, new Vector3(0, 0, 0));
    }

    public MapPart createTerrain(Model model, String id, Vector3 pos) {
	models.put(id, model);
	MapPart currentMapInstance = new MapPart(model, pos);
	bodyConstructionInfo.put(id, currentMapInstance.constructionInfo);
	instances.add(currentMapInstance);
	System.out.println("[Map]: models=" + models.size() + " instances=" + instances.size() + " rigidbodyInfos="
		+ bodyConstructionInfo.size());
	return currentMapInstance;
    }

    public MapPart createTerrain(String id, float width, float height, float depth, Vector3 pos, Color col) {
	Model model = ModelFactory.getGround(width, height, depth, col);
	return createTerrain(model, id, pos);
    }

    protected MapPart createMapObject(String g3dbPath, float x, float y, float z, float mass) {
	Model currentMap = models.get(g3dbPath);
	if (currentMap == null) {
	    currentMap = ModelFactory.getG3DBModel(g3dbPath);
	    models.put(g3dbPath, currentMap);
	}

	MapPart currentMapInstance = null;
	btRigidBody.btRigidBodyConstructionInfo currentInfo = bodyConstructionInfo.get(g3dbPath);
	if (currentInfo == null) {
	    currentMapInstance = new MapPart(currentMap, new Vector3(x, y, z), mass);
	    bodyConstructionInfo.put(g3dbPath, currentMapInstance.constructionInfo);
	} else {
	    currentMapInstance = new MapPart(currentMap, new Vector3(x, y, z), currentInfo);
	}
	instances.add(currentMapInstance);
	System.out.println("[Map]: models=" + models.size() + " instances=" + instances.size() + " rigidbodyInfos="
		+ bodyConstructionInfo.size());
	return currentMapInstance;
    }

    public MapPart getGround() {
	if (instances == null || instances.size() == 0)
	    return null;
	return instances.get(0);
    }

    public void print() {
    }

    protected void printNode(Node node) {
    }

    public void dispose() {
	Collection<Model> mods = models.values();
	for (Model m : mods)
	    m.dispose();
	for (MapPart p : instances)
	    p.dispose();
	models.clear();
	instances.clear();
    }

    public List<? extends GameModelInstance> getRenderingInstances() {
	return this.instances;
    }
}
