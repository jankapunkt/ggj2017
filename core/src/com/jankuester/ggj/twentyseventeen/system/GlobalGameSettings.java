package com.jankuester.ggj.twentyseventeen.system;

public class GlobalGameSettings {
    
    //graphics fallback values
    public static int resolutionX = 1280;
    public static int resolutionY = 768;
    
    public static float aspect_ratio = 16/9;
    
    public static boolean enableVsync = true;
    
    
    //SOUNDS
    public static float loudeness_music = 0.6f;
    public static float loudeness_fx = 0.8f;
    
    public GlobalGameSettings() {
	
    }
    
    public boolean loadSettingsFile() {
	return false;
    }
}
