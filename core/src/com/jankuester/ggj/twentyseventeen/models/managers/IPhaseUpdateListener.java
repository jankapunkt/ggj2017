package com.jankuester.ggj.twentyseventeen.models.managers;

import com.jankuester.ggj.twentyseventeen.models.maps.Phase;

public interface IPhaseUpdateListener {
    void phaseCreated(Phase phase, int phaseType, String phasename, int index);
    void phaseDisposed(Phase phase);
}
