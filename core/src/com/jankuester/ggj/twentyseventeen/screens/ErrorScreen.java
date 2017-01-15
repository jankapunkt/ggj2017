package com.jankuester.ggj.twentyseventeen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.jankuester.ggj.twentyseventeen.screens.factories.ScreenComponentFactory;

public class ErrorScreen extends ScreenBase {

    private String errorMessage;
    private StackTraceElement[] stackTrace;

    public ErrorScreen() {
	super();
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
	this.stackTrace = stackTrace;
    }

    public StackTraceElement[] getStackTrace() {
	return stackTrace;
    }

    @Override
    public void create() {
	super.create();
	font = ScreenComponentFactory.defaultFont;
    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(0, 0, 0, 0);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	stage.act();

	spriteBatch.begin();
	if (this.backgroundImage != null) {
	    spriteBatch.draw(backgroundImage, 0, 0, width, height);
	}
	font.draw(spriteBatch, "ERROR", 20, height - 20);
	font.draw(spriteBatch, "Message: " + getErrorMessage(), 20, height - 40);
	if (this.stackTrace != null && this.stackTrace.length > 0) {
	    int count = 1;
	    for (StackTraceElement element : stackTrace) {
		String line = element.getClassName() + " - " + element.getMethodName()
			+ Integer.toString(element.getLineNumber());
		font.draw(spriteBatch, line, 20, height - 60 - (count * 20));
		count++;
	    }
	}
	spriteBatch.end();
    }

}
