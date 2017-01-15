package com.jankuester.ggj.twentyseventeen.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ModelMap {

    /**
     * Constants related to properties file entries of maps.
     * 
     * @author major
     *
     */
    public static final class Map {

	/**
	 * the easy map
	 */
	public static final String MAP_EASY = "map_easy";

	/**
	 * the medium map
	 */
	public static final String MAP_MEDIUM = "map_medium";

	/**
	 * the hard map
	 */
	public static final String MAP_HARD = "map_hard";
    }

    /**
     * Constants related to properties file entries of Vehicles.
     * 
     * @author major
     *
     */
    public static final class Vehicle {

	/**
	 * lightweight vehicle
	 */
	public static final String VEHICLE_LIGHTWEIGHT = "vehicle_light";

	/**
	 * medium vehicle
	 */
	public static final String VEHICLE_MIDDLEWEIGHT = "vehicle_middle";

	/**
	 * heavy vehicle
	 */
	public static final String VEHICLE_HEAVYWEIGHT = "vehicle_heavy";
    }

    /**
     * Values to be combined with map or vehicle constants
     * 
     * @author major
     *
     */
    public static final class Value {

	/**
	 * id, everyone needs an id
	 */
	public static final String ID = "id";

	/**
	 * to be used to get the model for rendering in the preview screen
	 */
	public static final String PREVIEW = "preview";

	/**
	 * to be used to get the model for rendering in the game
	 */
	public static final String MODELS = "models";

	/**
	 * Name of the model
	 */
	public static final String NAME = "name";

	/**
	 * Description of the model
	 */
	public static final String DESCRIPTION = "description";

	/**
	 * Difficulty of the map in percent of 100
	 */
	public static final String DIFFICULTY = "difficutly";

	/**
	 * Agility of the vehicle in percent of 100
	 */
	public static final String AGILITY = "agility";

	/**
	 * Max speed of the vehicle in percent of 100
	 */
	public static final String SPEED = "speed";

	/**
	 * Shield power of the vehicle in percent of 100
	 */
	public static final String SHIELD = "shield";

	/**
	 * Required points to unlock this vehicle
	 */
	public static final String REQUIRED_POINTS = "required_points";
    }

    public static final class Object {
	public static final String OBJECT = "object";

    }

    ///////////////////////////////////////////////////////////////////////
    //
    // PROPERTIES MAP
    //
    ///////////////////////////////////////////////////////////////////////

    private static final Properties properties = new Properties();

    public static void loadFromProperties(String path) throws IOException, FileNotFoundException {
	FileHandle filehandle = Gdx.files.internal(path);
	if (!filehandle.exists())throw new FileNotFoundException("file "+path + " not found");
	InputStream inputStream = filehandle.read();
	properties.load(inputStream);
    }

    public static String get(String modelType, String identifier) {
	return properties.getProperty(modelType + "_" + identifier);
    }
    
    public static String getId(String modelType) {
	return properties.getProperty(modelType + "_" + Value.ID);
    }
    
    public static String getName(String modelType) {
	return properties.getProperty(modelType + "_" + Value.NAME);
    }
    
    public static String getDescription(String modelType) {
	return properties.getProperty(modelType + "_" + Value.DESCRIPTION);
    }
    
    public static int getDifficulty(String modelType) {
	return Integer.parseInt(properties.getProperty(modelType + "_" + Value.DIFFICULTY));
    }
    
    public static int getSpeed(String modelType) {
	return Integer.parseInt(properties.getProperty(modelType + "_" + Value.SPEED));
    }    
    
    public static int getShield(String modelType) {
	return Integer.parseInt(properties.getProperty(modelType + "_" + Value.SHIELD));
    }    
    
    public static int getAgility(String modelType) {
	return Integer.parseInt(properties.getProperty(modelType + "_" + Value.AGILITY));
    }
    
    public static String getModelPath(String modelType) {
	return properties.getProperty(modelType + "_" + Value.MODELS);
    }    
    
    public static String getPreviewPath(String modelType) {
	return properties.getProperty(modelType + "_" + Value.PREVIEW);
    }        
       
}
