package com.jankuester.ggj.twentyseventeen.screens;

import com.jankuester.ggj.twentyseventeen.models.entities.EntityManager;

public class GameScreen extends ScreenBase  {

    private final EntityManager entityManager;
    private String mapId;
    private String vehicleId;
    
    public GameScreen() {
	entityManager = new EntityManager();
    }
    
    public void setMapId(String mapId) {
	this.mapId = mapId;
    }
    
    public void setVehicleId(String vehicleId) {
	this.vehicleId = vehicleId;
    }
}
