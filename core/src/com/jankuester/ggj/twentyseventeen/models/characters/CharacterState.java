package com.jankuester.ggj.twentyseventeen.models.characters;

import com.badlogic.gdx.math.Matrix4;
import com.jankuester.ggj.twentyseventeen.models.items.states.WeaponState;

public class CharacterState {

    public Matrix4 currentTransform;
    public WeaponState currentWeapon;
    public HealthState healthState;

    public CharacterState(WeaponState wstate, HealthState healthState, Matrix4 transform) {
	this.currentWeapon = wstate;
	this.healthState = healthState;
	this.currentTransform = transform;
    }
}
