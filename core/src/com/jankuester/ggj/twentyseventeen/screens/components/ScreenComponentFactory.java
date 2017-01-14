package com.jankuester.ggj.twentyseventeen.screens.components;

import java.util.ArrayList;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class ScreenComponentFactory {
    private static final BitmapFont defaultFont = new BitmapFont();
    private static final ArrayList<BitmapFont> fonts = new ArrayList<BitmapFont>();

    private static final TextButtonStyle defaultTextButtonStyle = new TextButtonStyle();
    private static final LabelStyle defaultLabelStyle = new LabelStyle();

    private static boolean initiated = false;

    public static void init() {

	// fonts.add(createFont("assets/fonts/XOLONIUM-REGULAR.OTF",
	// Color.BLACK, Color.BLACK, Color.WHITE, 32));

	defaultFont.setColor(Color.BLACK);

	defaultTextButtonStyle.font = defaultFont;
	defaultTextButtonStyle.overFontColor = Color.ORANGE;
	defaultTextButtonStyle.fontColor = Color.BLACK;

	defaultLabelStyle.font = defaultFont;
	defaultLabelStyle.fontColor = Color.BLACK;

	initiated = true;
    }

    public static BitmapFont createFont(String fontFilePath, Color fontColor, Color borderColor, Color shadowColor,
	    int size) {
	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFilePath));
	FreeTypeFontParameter parameter15 = new FreeTypeFontParameter();
	parameter15.color = fontColor;
	parameter15.size = size;
	parameter15.borderColor = borderColor;
	parameter15.shadowColor = shadowColor;
	BitmapFont font = generator.generateFont(parameter15);
	generator.dispose();

	return font;
    }

    public static Label setLabelBackgroundColor(Label label, Color color, float alpha) {
	color.a = alpha;
	Pixmap labelColor = new Pixmap((int) label.getWidth(), (int) label.getHeight(), Format.RGBA8888);
	labelColor.setColor(color);
	labelColor.fill();
	label.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
	return label;
    }
    
    public static TextButton setButtonBackgroundColor(TextButton button, Color color, float alpha) {
	return (TextButton)setTableBackgroundColor(button, color, alpha);
    }
    
    public static Table setTableBackgroundColor(Table table, Color color, float alpha) {
	color.a = alpha;
	Pixmap labelColor = new Pixmap((int) table.getWidth(), (int) table.getHeight(), Format.RGBA8888);
	labelColor.setColor(color);
	labelColor.fill();
	table.setBackground(new Image(new Texture(labelColor)).getDrawable()); 
	return table;
    }
    

    public static TextButtonStyle defaultTextButtonStyle() {
	if (!initiated)
	    init();
	return defaultTextButtonStyle;
    }

    public static LabelStyle defaultLabelStyle() {
	if (!initiated)
	    init();
	return defaultLabelStyle;
    }

    public static LabelStyle customLabelStyle(BitmapFont font, Color fontColor) {
	if (!initiated)
	    init();
	LabelStyle style = new LabelStyle(font, fontColor);
	return style;
    }

    public static Label createLabel(String text) {
	return createLabel(text, defaultLabelStyle());
    }

    public static Label createLabel(String text, Color fontColor, float size) {
	Label l = createLabel(text, defaultLabelStyle());
	l.setColor(fontColor);
	l.setFontScale(size);
	return l;
    }

    public static Label createLabel(String text, LabelStyle style) {
	if (!initiated)
	    init();
	Label label = new Label(text, style);
	label = setLabelBackgroundColor(label, Color.DARK_GRAY);
	return label;
    }

    public static TextButton createTextButton(String text) {
	if (!initiated)
	    init();
	return createTextButton(text, 300, 100, Color.BLACK, 0, 0);
    }

    public static TextButton createTextButton(String text, int width, int height, Color col, int posx, int posy) {
	if (!initiated)
	    init();
	TextButton tb = new TextButton(text, defaultTextButtonStyle());
	tb.setWidth(width);
	tb.setHeight(height);
	tb.setColor(col);
	tb.setPosition(posx, posy);
	return tb;
    }

    public static ScreenMenuTextButton createMenuButton(int actionId, String text, Color col) {
	if (!initiated)
	    init();
	ScreenMenuTextButton gmtb = new ScreenMenuTextButton(text, defaultTextButtonStyle(), actionId);
	gmtb.setColor(col);
	gmtb.setPosition(0, 0);
	return gmtb;
    }

}
