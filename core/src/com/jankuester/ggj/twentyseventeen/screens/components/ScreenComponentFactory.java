package com.jankuester.ggj.twentyseventeen.screens.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class ScreenComponentFactory {
    private static final BitmapFont defaultFont = new BitmapFont();// Gdx.files.internal("assets/font/XOLONIUM-REGULAR.OTF")
    private static final TextButtonStyle defaultTextButtonStyle = new TextButtonStyle();

    private static boolean initiated = false;

    public static void init() {

	defaultFont.setColor(Color.BLACK);
	defaultTextButtonStyle.font = defaultFont;
	defaultTextButtonStyle.overFontColor = Color.ORANGE;
	defaultTextButtonStyle.fontColor = Color.BLACK;

	initiated = true;
    }

    public static TextButtonStyle defaultTextButtonStyle() {
	if (!initiated) init();
	return defaultTextButtonStyle;
    }

    public static TextButton createTextButton(String text) {
	if (!initiated) init();
	return createTextButton(text, 300, 100, Color.BLACK, 0, 0);
    }

    public static TextButton createTextButton(String text, int width, int height, Color col, int posx, int posy) {
	if (!initiated) init();
	TextButton tb = new TextButton(text, defaultTextButtonStyle());
	tb.setWidth(width);
	tb.setHeight(height);
	tb.setColor(col);
	tb.setPosition(posx, posy);
	return tb;
    }

    public static ScreenMenuTextButton createMenuButton(int actionId, String text, Color col) {
	if (!initiated) init();
	ScreenMenuTextButton gmtb = new ScreenMenuTextButton(text, defaultTextButtonStyle(), actionId);
	gmtb.setColor(col);
	gmtb.setPosition(0, 0);
	return gmtb;
    }

}
