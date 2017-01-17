package com.jankuester.ggj.twentyseventeen.models.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class ModelFactory {
    public static Model getG3DBModel(String g3dbPath) { //TODO throws exceptions and so on
	UBJsonReader jsonReader = new UBJsonReader();
	jsonReader.oldFormat = true;
	G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
	Model m = modelLoader.loadModel(Gdx.files.getFileHandle(g3dbPath, Files.FileType.Internal));
	
//	Material mat = m.materials.get(0);
//	mat.set(new ColorAttribute(ColorAttribute.Diffuse, Color.RED));
	return m;
    }
    
    public static GameModelInstance getGameModelInstance(Model model, Vector3 initialPos) { //TODO throws exceptions and so on
	return new GameModelInstance(model, initialPos);
    }
    
    public static GameModelInstance getGameModelInstance(String g3dbPath, Vector3 initialPos) { //TODO throws exceptions and so on
	return new GameModelInstance(getG3DBModel(g3dbPath), initialPos);
    }
    
    public static GameModelInstance getGameModelInstance(String g3dbPath, float x, float y, float z) { //TODO throws exceptions and so on
	return new GameModelInstance(getG3DBModel(g3dbPath), new Vector3(x, y, z));
    }

    public static Model getHumanG3DBModel(String g3dbPath) { //TODO throws exceptions and so on
	Model m = getG3DBModel(g3dbPath);
	// modify model here
	return m;
    }
    
    
    public static Model getGround(float w, float h, float d, Color c) {
	ModelBuilder modelBuilder = new ModelBuilder();
        return modelBuilder.createBox(w,h,d, 
                new Material(ColorAttribute.createDiffuse(c)),
                Usage.Position | Usage.Normal);
    }
    
    public static Material getGroundMaterial(){
	Material mat = new Material();
	mat.set( new ColorAttribute(ColorAttribute.Diffuse, Color.RED));
	return mat;
    }
}
