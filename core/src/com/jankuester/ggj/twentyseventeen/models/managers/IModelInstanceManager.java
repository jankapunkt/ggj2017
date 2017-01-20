package com.jankuester.ggj.twentyseventeen.models.managers;

import java.util.List;

import com.jankuester.ggj.twentyseventeen.models.GameModelInstance;

public interface IModelInstanceManager {
    public List<? extends GameModelInstance> getRenderingInstances();
}
