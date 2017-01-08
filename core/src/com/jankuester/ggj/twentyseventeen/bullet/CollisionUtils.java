package com.jankuester.ggj.twentyseventeen.bullet;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.AllHitsRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.RayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectConstArray;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

public class CollisionUtils {

    // --------------------------------------------------------//
    // RAY CAST DETECTION
    // --------------------------------------------------------//

    // static constants in memory
    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static final ClosestRayResultCallback callbackClose = new ClosestRayResultCallback(rayFrom, rayTo);
    private static final AllHitsRayResultCallback callbackAll = new AllHitsRayResultCallback(rayFrom, rayTo);

    /**
     * Gets the closest collision object. Credit goes to:
     * http://stackoverflow.com/users/1816356/noone
     * http://stackoverflow.com/questions/24988852/raycasting-in-libgdx-3d/
     * 24989069#24989069
     * 
     * @param collisionWorld
     *            The current collision world
     * @param ray
     *            The ray to shoot
     * @return The collision object which was hit by the ray first, returns null
     *         if nothing has been hit.
     */
    public static btCollisionObject rayTestClosestSingle(btCollisionWorld collisionWorld, Ray ray) {
	rayFrom.set(ray.origin);
	// 250 meters max from the origin
	rayTo.set(ray.direction).scl(250f).add(rayFrom);
	
	// we reuse the ClosestRayResultCallback,
	// thus we need to reset its values
	callbackClose.setCollisionObject(null);
	callbackClose.setClosestHitFraction(1f);

	final Vector3 rayFromWorld = new Vector3();
	rayFromWorld.set(rayFrom.x, rayFrom.y, rayFrom.z);
	Vector3 rayToWorld = new Vector3();
	rayToWorld.set(rayTo.x, rayTo.y, rayTo.z);

	callbackClose.getRayFromWorld(rayFromWorld);
	callbackClose.getRayToWorld(rayToWorld);

	collisionWorld.rayTest(rayFrom, rayTo, callbackClose);

	if (callbackClose.hasHit()) {
	    btCollisionObject colObj = callbackClose.getCollisionObject();
	    cleanup(callbackClose);
	    return colObj;
	}
	cleanup(callbackClose);
	return null;
    }

    //TODO use mask/filter instead of lists!
    public static btCollisionObject rayTestClosestAll(btCollisionWorld collisionWorld, Ray ray, short[] excludes){
	rayFrom.set(ray.origin);
	// 250 meters max from the origin
	rayTo.set(ray.direction).scl(250f).add(rayFrom);
	
	// we reuse the ClosestRayResultCallback,
	// thus we need to reset its values
	callbackAll.setCollisionObject(null);
	callbackAll.setClosestHitFraction(1f);
	callbackAll.setCollisionFilterGroup(excludes[1]);//TODO shift to include all excludes
	
	final Vector3 rayFromWorld = new Vector3();
	rayFromWorld.set(rayFrom.x, rayFrom.y, rayFrom.z);
	Vector3 rayToWorld = new Vector3();
	rayToWorld.set(rayTo.x, rayTo.y, rayTo.z);

	callbackAll.getRayFromWorld(rayFromWorld);
	callbackAll.getRayToWorld(rayToWorld);

	collisionWorld.rayTest(rayFrom, rayTo, callbackAll);

	
	if (callbackAll.hasHit()) {
	    btCollisionObjectConstArray collisionObjects = callbackAll.getCollisionObjects();
	    int count = 0;
	    btCollisionObject current = collisionObjects.at(count);
	    while(current!=null){
		//TODO double check collision flags and excludes
		//	and skip object if matching exists
//		if (current.getCollisionFlags() ){
//		    cleanup(callbackAll);
//		    return current;
//		}
//		count++;
		System.out.println("ray collision with flag: "+ current.getCollisionFlags());
		return current;
	    }
	}
	cleanup(callbackAll);
	return null;
    }
    
    private static void setup(RayResultCallback callback){
	
    }
    
    private static void cleanup(RayResultCallback callback){
	callback.setCollisionObject(null);
	rayFrom.setZero();
	rayTo.setZero();
    }
    
}
