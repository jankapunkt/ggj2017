package com.jankuester.ggj.twentyseventeen.models.factories;

import com.jankuester.ggj.twentyseventeen.logic.ModelMap;
import com.jankuester.ggj.twentyseventeen.models.utils.ModelPreview;

public class ModelPreviewFactory {

    public static ModelPreview createMapPreview(String modelType) {
	ModelPreview cityMapPreview = new ModelPreview(
		ModelFactory.getGameModelInstance(ModelMap.getPreviewPath(modelType), 0, 0, 0), ModelMap.MAP);
	cityMapPreview.setName(ModelMap.getName(modelType));
	cityMapPreview.setDescription(ModelMap.getDescription(modelType));
	cityMapPreview.setDifficulty(ModelMap.getDifficulty(modelType));
	cityMapPreview.setId(ModelMap.getId(modelType));
	return cityMapPreview;
    }

    public static ModelPreview createVehiclePreview(String modelType) {
	ModelPreview cityMapPreview = new ModelPreview(
		ModelFactory.getGameModelInstance(ModelMap.getPreviewPath(modelType), 0, 0, 0), ModelMap.VEHICLE);
	cityMapPreview.setName(ModelMap.getName(modelType));
	cityMapPreview.setDescription(ModelMap.getDescription(modelType));
	cityMapPreview.setId(ModelMap.getId(modelType));
	cityMapPreview.setSpeed(ModelMap.getSpeed(modelType));
	cityMapPreview.setAgility(ModelMap.getAgility(modelType));
	cityMapPreview.setShield(ModelMap.getShield(modelType));
	return cityMapPreview;
    }
}
