package com.jankuester.ggj.twentyseventeen.bullet;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public interface ICollidable {

	public btRigidBody getBody();
	public void setContactInfo(btRigidBody.btRigidBodyConstructionInfo contactInfo);
}
