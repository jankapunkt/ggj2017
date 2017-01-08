package com.jankuester.ggj.twentyseventeen.models.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

public class ModelFactory {
    public static Model getG3DBModel(String g3dbPath) {
	UBJsonReader jsonReader = new UBJsonReader();
	jsonReader.oldFormat = true;
	G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
	Model m = modelLoader.loadModel(Gdx.files.getFileHandle(g3dbPath, Files.FileType.Internal));
	Material mat = m.materials.get(0);

	return m;
    }

    public static Model getHumanG3DBModel(String g3dbPath) {
	Model m = getG3DBModel(g3dbPath);
	// modify model here
	return m;
    }
}
