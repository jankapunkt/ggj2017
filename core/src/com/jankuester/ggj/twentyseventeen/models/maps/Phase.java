package com.jankuester.ggj.twentyseventeen.models.maps;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.jankuester.ggj.twentyseventeen.models.environment.SceneObject;

public class Phase {

    public static final int TYPE_CLEAR = 0;
    public static final int TYPE_SPEED = 1;
    public static final int TYPE_OBSTACLES = 2;
    public static final int TYPE_INNOCENTS = 3;

    public static String getPhasename(int phaseType) {
	switch (phaseType) {
	case 0:
	    return "clear";
	case 1:
	    return "speed";
	case 2:
	    return "obstacles";
	case 3:
	    return "innocents";
	default:
	    throw new Error("unknown phasetype");
	}
    }

    public static Color getPhaseColor(int phaseType, boolean useComplementary) {
	switch (phaseType) {
	case Phase.TYPE_CLEAR:
	    return useComplementary ? Color.BLUE : Color.ORANGE;
	case Phase.TYPE_INNOCENTS:
	    return useComplementary ? Color.SALMON : Color.OLIVE;
	case Phase.TYPE_OBSTACLES:
	    return useComplementary ? Color.SKY : Color.SCARLET;
	case Phase.TYPE_SPEED:
	    return useComplementary ? Color.FOREST : Color.GOLD;
	default:
	    throw new Error("unknown phasetype");
	}
    }

    public HashMap<String, RaceCourseObject> courseObjects = new HashMap<String, RaceCourseObject>(16);
    public HashMap<String, SceneObject> sceneObjects = new HashMap<String, SceneObject>(32);

    public int phaseId;
    public int phaseType;

    public Phase(int phaseIndex, int phaseType) {
	this.phaseId = phaseIndex;
	this.phaseType = phaseType;
    }

    public void removeAll() {
	//TODO do I need this?
    }

    public void update() {
	for (RaceCourseObject raceCourseObj : courseObjects.values()) {
	    raceCourseObj.getBody().activate(true);
	}
    }
}
