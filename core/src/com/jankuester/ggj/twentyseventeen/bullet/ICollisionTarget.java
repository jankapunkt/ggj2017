package com.jankuester.ggj.twentyseventeen.bullet;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public interface ICollisionTarget {
    boolean isCollisionTarget(btCollisionObject toCompare);
}
