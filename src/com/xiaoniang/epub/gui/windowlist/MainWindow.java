package com.xiaoniang.epub.gui.windowlist;

import com.xiaoniang.epub.resources.Links;
import com.xiaoniang.epub.resources.Log;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindow extends Application {

	@Override
	public void start(Stage stage) {
		initUI(stage);
	}

	private void initUI(Stage stage) {

		ArrayList<String> strings = new ArrayList<String>();
		Links.fill();
		MainWindowController mainWindowController = new MainWindowController();
		try {
			mainWindowController.setListView();
		} catch (IOException e) {
			e.printStackTrace(Log.stream());
		}
		stage.setScene(mainWindowController.getScene());
		stage.show();
	}

	public static void create(String[] args) {
		launch(args);
	}
}
