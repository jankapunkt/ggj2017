package com.jankuester.ggj.twentyseventeen.models.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class ModelPreview {

    private final String name;
    private final String description;
    private final int difficulty;
    private final GameModelInstance model;
    
    private float scale = 1.0f;

    public ModelPreview(String name, String description, int difficulty, GameModelInstance model) {
	this.name = name;
	this.description = description;
	this.difficulty = difficulty;
	this.model = model;
    }

    public ModelPreview(FileHandle propertiesFile) {
	this.name = null;
	this.description = null;
	this.difficulty = 0;
	this.model = null;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public int getDifficulty() {
	return difficulty;
    }

    public GameModelInstance getModel() {
	return model;
    }

    public float getScale() {
	return scale;
    }
    
    public void setScale(float scale) {
	this.scale = scale;
	this.model.transform.scl(scale);
    }
}
