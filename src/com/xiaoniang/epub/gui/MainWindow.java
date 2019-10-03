package com.xiaoniang.epub.gui;

import com.xiaoniang.epub.resources.Links;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MainWindow extends Application {

	@Override
	public void start(Stage stage) {
		initUI(stage);
	}

	private void initUI(Stage stage) {
		
		System.out.println("GUI is initiated");
		ArrayList<String> strings = new ArrayList<String>();
		Links.fill();
		MainWindowController mainWindowController = new MainWindowController();
		mainWindowController.setListView();
		/*root.setStyle("-fx-background-color: BEIGE;");  
		Scene scene = new Scene(root, 1024, 600);
		scene.setFill(Color.CYAN);
		stage.setTitle("Main Window");
		stage.setScene(scene);*/
		stage.setScene(mainWindowController.getScene());
		stage.show();
	}

	public static void create(String[] args) {
		launch(args);
	}
}
