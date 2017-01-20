package com.jankuester.ggj.twentyseventeen.screens;

import com.badlogic.gdx.Gdx;

/**
 * @author jan kuester <jkuester@uni-bremen.de>
 */
public class StartScreen extends ScreenBase {
    public StartScreen() {
	super();
    }

    public static final int MENU_ALIGN_TOP = 0;
    public static final int MENU_ALIGN_MIDDLE = 1;
    public static final int MENU_ALIGN_BOTTOM = 2;

    private int currentAlign;

    public void alignMenu(int i) {
	currentAlign = i;
    }

    @Override
    public void create() {
	// TODO Auto-generated method stub
	super.create();
	int tableYPos = 0;
	switch (currentAlign) {
	case MENU_ALIGN_TOP:
	    tableYPos = Gdx.graphics.getHeight() * 2 / 3;
	    break;
	case MENU_ALIGN_MIDDLE:
	    tableYPos = Gdx.graphics.getHeight() / 2;
	    break;
	case MENU_ALIGN_BOTTOM:
	    tableYPos = Gdx.graphics.getHeight() / 6;
	    break;
	default:
	    break;
	}
	menuTable.setPosition(Gdx.graphics.getWidth() / 2, tableYPos);
    }
}
