package com.jankuester.ggj.twentyseventeen.models.utils;

import com.badlogic.gdx.files.FileHandle;
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

    public String getDifficulty() {
	switch (difficulty) {
	case 0:
	    return "easy";
	case 1:
	    return "medium";
	case 2:
	    return "hard";
	default:
	    throw new Error("no difficulty set");
	}
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
