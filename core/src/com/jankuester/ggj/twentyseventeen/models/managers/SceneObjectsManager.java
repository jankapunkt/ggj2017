package com.jankuester.ggj.twentyseventeen.models.managers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;
import com.jankuester.ggj.twentyseventeen.models.environment.SceneObject;
import com.jankuester.ggj.twentyseventeen.models.environment.Sun;
import com.jankuester.ggj.twentyseventeen.models.factories.AttributeFactory;
import com.jankuester.ggj.twentyseventeen.models.factories.MaterialFactory;
import com.jankuester.ggj.twentyseventeen.models.factories.ModelFactory;
import com.jankuester.ggj.twentyseventeen.models.maps.Phase;
import com.jankuester.ggj.twentyseventeen.models.maps.RaceCourseObject;
import com.jankuester.ggj.twentyseventeen.system.Utils;

public class SceneObjectsManager extends BaseModelInstanceManager implements IModelInstanceManager {

    private final ArrayList<GameModelInstance> instances = new ArrayList<GameModelInstance>();

    private int mapSize;

    public SceneObjectsManager(int mapSize) {
	this.mapSize = mapSize;
    }

    public List<? extends GameModelInstance> getRenderingInstances() {
	return this.instances;
    }

    public void addInstance(GameModelInstance instance) {
	instances.add(instance);
    }

    int maxObjectsPerPhase = 4;
    int minObjectsPerPhase = 2;

    public void createPhase(Phase phase, int phaseType, String phaseName, int index) {
	Color phaseColor = Phase.getPhaseColor(phaseType, true);
	phaseColor.a = 0.5f;
	PointLight pl = ModelFactory.createPointLight(0, 26, index * mapSize / 2, Color.WHITE, 50f);
	Attributes attributes = new Attributes();
	attributes.set(ColorAttribute.createDiffuse(phaseColor), AttributeFactory.getPointLightAttribute(pl));
	Material sceneMaterial = MaterialFactory.createMaterial(phaseName, attributes);

	int startZ = (index*mapSize);
	int numLeft = Utils.random(minObjectsPerPhase, maxObjectsPerPhase);
	int numRight = Utils.random(minObjectsPerPhase, maxObjectsPerPhase);
	float sizeRight = mapSize/numRight;
	float sizeLeft = mapSize/numLeft;
	String indexName = Integer.toString(index);
	
	final Vector3 dimensions = new Vector3();
	final Vector3 position = new Vector3();
	
	for (int i = 0; i < numLeft; i++) {
	    int height = Utils.random(32, 64);
	    dimensions.set(sizeLeft,height,sizeLeft);
	    position.set(-mapSize - sizeLeft*2, 0, startZ + i*sizeLeft);
	    SceneObject sceneObj = createSceneObject("scene_left" + Integer.toString(height), 
		    phaseName,dimensions, position, sceneMaterial);
	    String objId = "scene_left"+indexName;
	    sceneObj.setId(objId);
	    phase.sceneObjects.put(objId, sceneObj);
	    instances.add(sceneObj);
	}
	
	for (int i = 0; i < numRight; i++) {
	    int height = Utils.random(32, 64);
	    dimensions.set(sizeRight,height,sizeRight);
	    position.set(mapSize + sizeRight*2, 0, startZ + i*sizeRight);
	    SceneObject sceneObj = createSceneObject("scene_left" + Integer.toString(height), 
		    phaseName,dimensions, position, sceneMaterial);
	    String objId = "scene_left"+indexName;
	    sceneObj.setId(objId);
	    phase.sceneObjects.put(objId, sceneObj);
	    instances.add(sceneObj);
	}
    }
    
    public void disposePhase(Phase phase){
	instances.removeAll(phase.sceneObjects.values());
    }

    public SceneObject createSceneObject(String id, String phaseName, Vector3 dimensions, Vector3 position, Material mat) {
	String materialKey = id + phaseName;
	Model currentModel = models.get(materialKey);
	if (currentModel == null) {
	    currentModel = ModelFactory.createBoxModel(materialKey, dimensions.x, dimensions.y, dimensions.z, mat);
	    models.put(materialKey, currentModel);
	}
	return new SceneObject(currentModel, position);
    }
}
