package com.jankuester.ggj.twentyseventeen.models.items.states;

import java.util.HashMap;
import java.util.Iterator;

public class WeaponStates {
	
	HashMap<String, WeaponState> weapons;
	
	
	public WeaponStates() 
	{
		weapons = new HashMap<String, WeaponState>();
	}
	
	public Iterator<WeaponState> getAll()
	{
		return weapons.values().iterator();
	}
	
	
	/**
	 * Adds a new weapon or adds its ammo values to existing weapon
	 * @param key
	 * @param wState
	 */
	public void add(String key, WeaponState wState)
	{
		WeaponState state = get(key);
		if (state != null)
		{
			state.ammo += wState.ammo;
			return;
		}
		
		weapons.put(key, wState);
	}
	
	public int size()
	{
		return weapons.size();
	}
	
	public WeaponState get(String key)
	{
		return weapons.get(key);
	}

	
}
