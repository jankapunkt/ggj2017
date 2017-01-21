package com.jankuester.ggj.twentyseventeen.models.managers;

import java.util.List;

import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public interface IModelInstanceManager {
    public void addInstance(GameModelInstance instance);
    public List<? extends GameModelInstance> getRenderingInstances();
}
