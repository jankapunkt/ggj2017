package com.jankuester.ggj.twentyseventeen.screens.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class UIFactory {

    ////////////////////////////////////////////////////////////////////
    //
    // CONSTANTS / DEFAULTS
    //
    ////////////////////////////////////////////////////////////////////

    // FONTS
    public static final BitmapFont defaultFont = new BitmapFont();
    public static final BitmapFont largeFont = createFont("fonts/XOLONIUM-REGULAR.OTF", Color.GREEN, Color.BLACK,
	    Color.WHITE, 48);
    public static final BitmapFont mediumFont = createFont("fonts/XOLONIUM-REGULAR.OTF", Color.GREEN, Color.BLACK,
	    Color.WHITE, 28);

    // DRWABLES
    public static final Drawable defaultBackground = createBackground(Color.WHITE, 0.7f, 200, 100);

    // UI ELEMENT STYLES
    public static final TextButtonStyle defaultTextButtonStyle = createTextButtonStyle(largeFont, Color.BLACK,
	    Color.GREEN, Color.GREEN, Color.DARK_GRAY);
    public static final LabelStyle defaultLabelStyle = createLabelStyle(defaultFont, null);

    private static boolean initiated = false;

    public static void init() {
	initiated = true;
    }

    ////////////////////////////////////////////////////////////////////
    //
    // FONTS
    //
    ////////////////////////////////////////////////////////////////////

    public static BitmapFont createFont(String fontFilePath, Color fontColor, Color borderColor, Color shadowColor,
	    int size) {
	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFilePath));
	FreeTypeFontParameter parameter = new FreeTypeFontParameter();
	parameter.color = fontColor;
	parameter.size = size;
	parameter.borderColor = borderColor;
	parameter.shadowColor = shadowColor;
	BitmapFont font = generator.generateFont(parameter);
	generator.dispose();
	return font;
    }

    ////////////////////////////////////////////////////////////////////
    //
    // BACKGROUND
    //
    ////////////////////////////////////////////////////////////////////

    public static Drawable createBackground(Color color, float alpha, int width, int height) {
	color.a = alpha;
	Pixmap bgColor = new Pixmap(width, height, Format.RGBA8888);
	bgColor.setColor(color);
	bgColor.fill();
	Image im = new Image(new Texture(bgColor));
	return im.getDrawable();
    }

    ////////////////////////////////////////////////////////////////////
    //
    // LABEL
    //
    ////////////////////////////////////////////////////////////////////

    public static LabelStyle defaultLabelStyle() {
	return defaultLabelStyle;
    }

    public static LabelStyle createLabelStyle(BitmapFont font, Drawable background) {
	LabelStyle style = new LabelStyle();
	style.font = font != null ? font : defaultFont;
	style.fontColor = Color.BLACK;
	if (background != null)
	    style.background = background;
	return style;
    }

    public static Label createLabel(String text) {
	return createLabel(text, defaultLabelStyle());
    }

    public static Label createLabel(String text, BitmapFont font, Drawable background) {
	Label label = new Label(text, createLabelStyle(font, background));
	return label;
    }

    public static Label createLabel(String text, Color fontColor, float size) {
	Label l = createLabel(text, defaultLabelStyle());
	l.setColor(fontColor);
	l.setFontScale(size);
	return l;
    }

    public static Label createLabel(String text, LabelStyle style) {
	Label label = new Label(text, style);
	return label;
    }

    ////////////////////////////////////////////////////////////////////
    //
    // TEXTBUTTON
    //
    ////////////////////////////////////////////////////////////////////

    public static TextButtonStyle defaultTextButtonStyle() {
	return defaultTextButtonStyle;
    }

    public static TextButtonStyle createTextButtonStyle(BitmapFont font, Color fontColor, Color overFontColor,
	    Color downFontColor, Color disabledFontColor) {
	TextButtonStyle style = new TextButtonStyle();
	style.font = font;
	style.fontColor = fontColor;
	style.overFontColor = overFontColor;
	style.downFontColor = downFontColor;
	style.disabledFontColor = disabledFontColor;
	return style;
    }

    public static TextButton createTextButton(String text, int width, int height, int posx, int posy) {
	return createTextButton(text, width, height, posx, posy, null);
    }

    public static TextButton createTextButton(String text, int width, int height, int posx, int posy,
	    Drawable background) {
	if (!initiated)
	    init();
	TextButton tb = new TextButton(text, defaultTextButtonStyle());
	tb.setWidth(width);
	tb.setHeight(height);
	tb.setPosition(posx, posy);
	if (background != null)
	    tb.setBackground(background);
	return tb;
    }

    public static ScreenMenuTextButton createMenuButton(int actionId, String text, int width, int height, int posx,
	    int posy, Drawable background) {
	ScreenMenuTextButton tb = new ScreenMenuTextButton(text, defaultTextButtonStyle(), actionId);
	tb.setWidth(width);
	tb.setHeight(height);
	tb.setPosition(posx, posy);
	if (background != null)
	    tb.setBackground(background);
	return tb;
    }

    public static ScreenMenuTextButton createMenuButton(int actionId, String text, int width, int height, int posx,
	    int posy) {
	return createMenuButton(actionId, text, width, height, posx, posy, null);
    }

}
