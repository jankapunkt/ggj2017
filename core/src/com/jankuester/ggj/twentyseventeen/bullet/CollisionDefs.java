package com.jankuester.ggj.twentyseventeen.bullet;

public class CollisionDefs {
	

	//---------------------- CONTACT FLAGS -------------------//
	
    public final static short GROUND_FLAG = 1<<8;
    public final static short OBJECT_FLAG = 1<<9;
    public final static short PLAYER_FLAG = 1<<10;
    public final static short ALL_FLAG = 1<<11;
    public final static short WALL_FLAG = 1<<12;
    public final static short WEAPON_FLAG= 1<<13;
    
    
	
	private static int value = 0;
	
	public static int generateUserValue()
	{
		return ++value;
	}
}
