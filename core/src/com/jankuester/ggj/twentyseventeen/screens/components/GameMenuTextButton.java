package com.jankuester.ggj.twentyseventeen.screens.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameMenuTextButton extends TextButton
{
	private final int action;

	private boolean disabled = false;
	
	public GameMenuTextButton(String text, Skin skin, byte action)
	{
		super(text, skin);
		this.action = action;
	}

	public GameMenuTextButton(String text, TextButtonStyle style, int action)
	{
		super(text, style);
		this.action = action;
	}

	public GameMenuTextButton(String text, Skin skin, String styleName, int action)
	{
		super(text, skin, styleName);
		this.action = action;
	}

	public int getAction()
	{
		return action;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}
}
