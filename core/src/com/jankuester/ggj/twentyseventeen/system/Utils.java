package com.jankuester.ggj.twentyseventeen.system;

import java.util.Random;

public class Utils {

    private static final Random random = new Random();

    public static int random(int min, int max) {
	max = max+1;
	return random.nextInt(max - min) + min;
    }
    
    public static boolean random(int max){
	float rand = random(0, max)/100;
	return Math.round(rand) == 1 ? true : false;
    }
}
