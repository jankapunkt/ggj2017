package com.jankuester.ggj.twentyseventeen.models.maps;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

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

    public static Color getPhaseColor(int phaseType) {
	switch (phaseType) {
	case Phase.TYPE_CLEAR:
	    return Color.ORANGE;
	case Phase.TYPE_INNOCENTS:
	    return Color.PINK;
	case Phase.TYPE_OBSTACLES:
	    return Color.RED;
	case Phase.TYPE_SPEED:
	    return Color.GREEN;
	default:
	    throw new Error("unknown phasetype");
	}
    }

    public HashMap<String, RaceCourseObject> phaseObjects = new HashMap<String, RaceCourseObject>(32);

    public int phaseId;

    public Phase(int phaseIndex) {
	this.phaseId = phaseIndex;
    }

    public void removeAll() {
	// dispose here?
    }
}
