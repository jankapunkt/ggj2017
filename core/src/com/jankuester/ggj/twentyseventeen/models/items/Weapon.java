package com.jankuester.ggj.twentyseventeen.models.items;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.jankuester.ggj.twentyseventeen.bullet.CollisionDefs;
import com.jankuester.ggj.twentyseventeen.models.characters.KinematicCharacter;
import com.jankuester.ggj.twentyseventeen.models.items.states.WeaponState;

public class Weapon extends Item {

    protected int power;

    protected int ammo;
    protected int maxammo;
    protected int magazine_size;

    protected String imagePath;

    public Weapon(Model model, float x, float y, float z, String id, String pathToCollectSound,
	    String pathToWeaponImage) {
	super(model, x, y, z, id, pathToCollectSound);
	power = 0;
	ammo = 0;
	maxammo = 0;
	magazine_size = 0;
	imagePath = pathToWeaponImage;

	colBody.setCollisionFlags(
		colBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
	colBody.setContactCallbackFlag(CollisionDefs.WEAPON_FLAG);
	colBody.setContactCallbackFilter(CollisionDefs.PLAYER_FLAG);
	colBody.setUserValue(CollisionDefs.generateUserValue());
    }

    /**
     * Sets the fire power of this weapon
     * 
     * @param pow
     */
    public void setPower(int pow) {
	power = pow;
    }

    /**
     * Sets the amount of ammo, when collecting this weapon
     * 
     * @param amount
     */
    public void setAmmo(int amount) {
	ammo = amount;
    }

    public void setMaxAmmo(int amount) {
	maxammo = amount;
    }

    public void setMagazineSize(int amount) {
	magazine_size = amount;
    }

    @Override
    public void onCollect(KinematicCharacter collector) {
	System.out.println("collect item sound");
	collector.addWeapon(getState());
	collectItemSound.play(1.0f);
    }
    

    @Override
    public void dispose() {
	super.dispose();
	collectItemSound.dispose();
    }

    public WeaponState getState() {
	WeaponState state = new WeaponState(imagePath);
	state.ammo = this.ammo;
	state.power = this.power;
	state.maxammo = this.maxammo;
	state.magazine_size = this.magazine_size;
	return state;
    }

}
