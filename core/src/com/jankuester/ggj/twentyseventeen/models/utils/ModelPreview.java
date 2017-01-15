package com.jankuester.ggj.twentyseventeen.models.utils;

import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public class ModelPreview {

    private final GameModelInstance model;
    private final String type;

    private String id;
    private String name;
    private String description;

    private int difficulty;
    private String _difficulty;
    
    private int agility;
    private String _agility;
    private int speed;
    private String _speed;
    private int shield;
    private String _shield;
    private float scale = 1.0f;


    public ModelPreview(GameModelInstance model, String type) {
	this.model = model;
	this.type = type;
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

    public void setId(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDescription() {
	return description;
    }

    public void setDifficulty(int difficulty) {
	this.difficulty = difficulty;
	this._difficulty = Integer.toString(difficulty);
    }

    public int getDifficulty() {
	return difficulty;
    }

    public String getDifficultyString() {
	return _difficulty;
    }

    public void setAgility(int agility) {
	this.agility = agility;
	this._agility = Integer.toString(agility);
    }

    public int getAgility() {
	return agility;
    }
    
    public String getAgilityString() {
	return _agility;
    }

    public void setSpeed(int speed) {
	this.speed = speed;
	this._speed= Integer.toString(speed);
    }

    public int getSpeed() {
	return speed;
    }
    
    public String getSpeedString() {
	return _speed;
    }

    public void setShield(int shield) {
	this.shield = shield;
	this._shield= Integer.toString(shield);
    }
    
    public String getShieldString() {
	return _shield;
    }

    public int getShield() {
	return shield;
    }

    public String getType() {
	return type;
    }


}
