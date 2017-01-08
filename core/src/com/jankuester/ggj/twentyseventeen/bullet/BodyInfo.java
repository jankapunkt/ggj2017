package com.jankuester.ggj.twentyseventeen.bullet;

import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class BodyInfo {
	
	public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
	public final btCollisionShape bodyShape;
	public final btMotionState motionState;
	
	public final String bodyKey;
	
	public BodyInfo(
			String key,
			btRigidBody.btRigidBodyConstructionInfo constructionInfo,
			btCollisionShape bodyShape,
			btMotionState motionState)
	{
		this.bodyKey = key;
		this.constructionInfo = constructionInfo;
		this.bodyShape = bodyShape;
		this.motionState = motionState;
	}
	
	public void dispose()
	{
		bodyShape.dispose();
		constructionInfo.dispose();
		motionState.dispose();
	}
}
