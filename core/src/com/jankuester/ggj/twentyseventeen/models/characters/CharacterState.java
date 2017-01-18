package com.jankuester.ggj.twentyseventeen.models.characters;

public class CharacterState {

    // public final Matrix4 currentTransform;
    // public WeaponState currentWeapon;
    // public HealthState healthState;
    public final float shield; // percent
    public final float speed;

    public CharacterState(float shield, float speed) {
	this.shield = shield;
	this.speed = speed;
    }

    @Override
    public String toString() {
	return "Cstate: shield=" + Float.toString(shield) + " speed=" + Float.toString(speed);
    }
}
