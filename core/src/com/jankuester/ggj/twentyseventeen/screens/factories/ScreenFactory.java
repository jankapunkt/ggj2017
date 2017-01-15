package com.jankuester.ggj.twentyseventeen.screens.factories;

import com.jankuester.ggj.twentyseventeen.screens.ErrorScreen;
import com.jankuester.ggj.twentyseventeen.screens.LoadingScreen;

public class ScreenFactory {

    public static LoadingScreen createLoadingScreen() {
	return new LoadingScreen();
    }

    public static ErrorScreen createErrorScreen(Exception e) {
	ErrorScreen err = new ErrorScreen();
	err.setErrorMessage(e.getMessage());
	err.setStackTrace(e.getStackTrace());
	return err;
    }

}
